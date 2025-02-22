package com.net.sparrow.service.sys;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.net.sparrow.dto.web.CityDTO;
import com.net.sparrow.entity.auth.AuthUserEntity;
import com.net.sparrow.entity.auth.CaptchaEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.auth.TokenEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.entity.email.RemoteLoginEmailEntity;
import com.net.sparrow.entity.sys.DeptConditionEntity;
import com.net.sparrow.entity.sys.DeptEntity;
import com.net.sparrow.entity.sys.JobEntity;
import com.net.sparrow.entity.sys.RoleEntity;
import com.net.sparrow.entity.sys.UserRoleConditionEntity;
import com.net.sparrow.entity.sys.UserRoleEntity;
import com.net.sparrow.enums.EmailTypeEnum;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.enums.TaskTypeEnum;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.GeoIpHelper;
import com.net.sparrow.helper.TokenHelper;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.mapper.sys.DeptMapper;
import com.net.sparrow.mapper.sys.JobMapper;
import com.net.sparrow.mapper.sys.RoleMapper;
import com.net.sparrow.mapper.sys.UserRoleMapper;
import com.net.sparrow.util.BetweenTimeUtil;
import com.net.sparrow.util.DateFormatUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.IpUtil;
import com.net.sparrow.util.PasswordUtil;
import com.net.sparrow.util.RedisUtil;
import com.net.sparrow.util.TokenUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.UserMapper;
import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import static com.net.sparrow.util.AssertUtil.ASSERT_ERROR_CODE;

/**
 * 用户 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
@Slf4j
public class UserService extends com.net.sparrow.service.BaseService<UserEntity, UserConditionEntity> {

	private static final String CAPTCHA_PREFIX = "captcha:";

	private static final String DEFAULT_PASSWORD = "123456";

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Value("${mall.mgt.captchaExpireSecond:60}")
	private int captchaExpireSecond;

	@Value("${mall.mgt.remoteLoginDiffHour:48}")
	private int remoteLoginDiffHour;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	private PasswordUtil passwordUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private GeoIpHelper geoIpHelper;

	@Autowired
	private CommonTaskMapper commonTaskMapper;

	@Autowired
	private JobMapper jobMapper;

	@Autowired
	private RoleMapper roleMapper;

	public TokenEntity login(AuthUserEntity authUserEntity) {
		String code = redisUtil.get(getCaptchaKey(authUserEntity.getUuid()));
		AssertUtil.hasLength(code, "该验证码不存在或者已失效");
		AssertUtil.isTrue(code.trim().equals(authUserEntity.getCode().trim()), "验证码错误");
		try {
			String decodeRsaPassword = passwordUtil.decodeRsaPassword(authUserEntity);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUserEntity.getUsername(), decodeRsaPassword);
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			JwtUserEntity jwtUserEntity = (JwtUserEntity) (authentication.getPrincipal());
			UserEntity userEntity = userMapper.findByUserName(jwtUserEntity.getUsername());
			AssertUtil.notNull(userEntity, "该用户不存在");
			//获取当前用户的IP
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String ip = IpUtil.getIpAddr(httpServletRequest);
			CityDTO cityDTO = geoIpHelper.getCity(ip);
			if (Objects.nonNull(cityDTO)) {
				String city = cityDTO.getCity();
				validateRemoteLogin(userEntity, city);
				userEntity.setLastLoginCity(city);
			}
			String token = tokenHelper.generateToken(jwtUserEntity);
			redisUtil.del(getCaptchaKey(authUserEntity.getUuid()));
			List<String> roles = jwtUserEntity.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
			//更新用户的最后登录城市
			updateLastLoginCity(userEntity);
			return new TokenEntity(authUserEntity.getUsername(), token, roles);
		}catch (Exception e) {
			log.info("登录失败：", e);
			if (e instanceof BusinessException) {
				throw e;
			}
			throw new BusinessException(ASSERT_ERROR_CODE, "用户名或密码错误");
		}

	}

	private void validateRemoteLogin(UserEntity userEntity, String nowCity) {
		if (!StringUtils.hasLength(userEntity.getLastLoginCity())) {
			return;
		}
		Date lastLoginTime = userEntity.getLastLoginTime();
		if (Objects.nonNull(lastLoginTime)) {
			long betweenHours = DateUtil.between(new Date(), lastLoginTime, DateUnit.HOUR);
			if (betweenHours > remoteLoginDiffHour) {
				return;
			}
		}

		//记录异地登录请求
		recordRemoteLoginData(userEntity, nowCity);
		//用户修改密码时可以将lastLoginCity清空
		AssertUtil.isTrue(userEntity.getLastLoginCity().equals(nowCity), "您的账号处于异地登录，为了安全考虑，请修改密码之后重新登录");
	}

	private void recordRemoteLoginData(UserEntity userEntity, String nowCity) {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		RemoteLoginEmailEntity remoteLoginEmailEntity = new RemoteLoginEmailEntity();
		remoteLoginEmailEntity.setUsername(userEntity.getUserName());
		remoteLoginEmailEntity.setNickName(userEntity.getNickName());
		remoteLoginEmailEntity.setIp(IpUtil.getIpAddr(httpServletRequest));
		remoteLoginEmailEntity.setDevice(httpServletRequest.getHeader("user-agent"));
		remoteLoginEmailEntity.setCityName(nowCity);
		remoteLoginEmailEntity.setEmail(userEntity.getEmail());
		remoteLoginEmailEntity.setLoginTime(DateFormatUtil.parseToString(userEntity.getUpdateTime()));

		CommonTaskEntity commonTaskEntity = createCommonTaskEntity();
		commonTaskEntity.setRequestParam(JSONUtil.toJsonStr(remoteLoginEmailEntity));
		commonTaskMapper.insert(commonTaskEntity);
	}

	private CommonTaskEntity createCommonTaskEntity() {
		CommonTaskEntity commonTaskEntity = new CommonTaskEntity();
		commonTaskEntity.setName("发送异地登录邮件");
		commonTaskEntity.setStatus(TaskStatusEnum.WAITING.getValue());
		commonTaskEntity.setFailureCount(0);
		commonTaskEntity.setType(TaskTypeEnum.SEND_EMAIL.getValue());
		commonTaskEntity.setBizType(EmailTypeEnum.REMOTE_LOGIN.getValue());
		FillUserUtil.fillCreateUserInfo(commonTaskEntity);
		return commonTaskEntity;
	}

	private void updateLastLoginCity(UserEntity userEntity) {
		FillUserUtil.fillUpdateUserInfo(userEntity);
		userEntity.setLastLoginTime(new Date());
		userMapper.update(userEntity);
	}

	public void logout(HttpServletRequest request) {
		String token = TokenUtil.getTokenForAuthorization(request);
		AssertUtil.hasLength(token, "Authorization不正确");
		tokenHelper.delToken(token);
	}

	public JwtUserEntity getUserInfo() {
		String currentUsername = tokenHelper.getCurrentUsername();
		return (JwtUserEntity) userDetailsService.loadUserByUsername(currentUsername);
	}

	/**
	 * 查询用户信息
	 *
	 * @param id 用户ID
	 * @return 用户信息
	 */
	public UserEntity findById(Long id) {
		return userMapper.findById(id);
	}

	/**
	 * 根据条件分页查询用户列表
	 *
	 * @param userConditionEntity 用户信息
	 * @return 用户集合
	 */
	public ResponsePageEntity<UserEntity> searchByPage(UserConditionEntity userConditionEntity) {
		BetweenTimeUtil.parseTime(userConditionEntity);
		int count = userMapper.searchCount(userConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(userConditionEntity);
		}
		List<UserEntity> dataList = userMapper.searchByCondition(userConditionEntity);
		fillData(dataList);
		return ResponsePageEntity.build(userConditionEntity, count, dataList);
	}

	private void fillData(UserEntity userEntity) {
		if (Objects.nonNull(userEntity.getDept())) {
			userEntity.setDeptId(userEntity.getDept().getId());
		}

		if (CollectionUtils.isNotEmpty(userEntity.getJobs())) {
			userEntity.setJobId(userEntity.getJobs().get(0).getId());
		}
	}

	private void fillData(List<UserEntity> dataList) {
		if(CollectionUtils.isEmpty(dataList)) {
			return;
		}
		List<Long> deptIdList = dataList.stream().map(UserEntity::getDeptId).filter(Objects::nonNull).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(deptIdList)) {
			return;
		}
		DeptConditionEntity deptConditionEntity = new DeptConditionEntity();
		deptConditionEntity.setIdList(deptIdList);
		List<DeptEntity> deptEntities = deptMapper.searchByCondition(deptConditionEntity);
		Map<Long, List<DeptEntity>> deptMap = deptEntities.stream().collect(Collectors.groupingBy(DeptEntity::getId));
		for (UserEntity userEntity : dataList) {
			if (Objects.isNull(userEntity.getDeptId())) {
				continue;
			}
			List<DeptEntity> deptEntityList = deptMap.get(userEntity.getDeptId());
			if (CollectionUtils.isNotEmpty(deptEntityList)) {
				DeptEntity deptEntity = deptEntityList.get(0);
				userEntity.setDeptName(deptEntity.getName());
				userEntity.setDept(deptEntity);
			}
		}
		fillJob(dataList);
		fillRole(dataList);
	}

	private void fillJob(List<UserEntity> dataList) {
		List<Long> jobIdList = dataList.stream().filter(x -> Objects.nonNull(x.getJobId())).map(UserEntity::getJobId).collect(Collectors.toList());
		List<JobEntity> jobList = jobMapper.findByIds(jobIdList);
		for (UserEntity userEntity : dataList) {
			Optional<JobEntity> optional = jobList.stream().filter(x -> x.getId().equals(userEntity.getJobId())).findAny();
			if (optional.isPresent()) {
				userEntity.setJobs(Lists.newArrayList(optional.get()));
			}

		}
	}

	private void fillRole(List<UserEntity> dataList) {
		List<Long> userIdList = dataList.stream().map(UserEntity::getId).collect(Collectors.toList());
		UserRoleConditionEntity userRoleConditionEntity = new UserRoleConditionEntity();
		userRoleConditionEntity.setUserIdList(userIdList);
		userRoleConditionEntity.setPageSize(0);
		//用户角色关联
		List<UserRoleEntity> userRoleEntityList = userRoleMapper.searchByCondition(userRoleConditionEntity);
		if (CollectionUtils.isEmpty(userRoleEntityList)) {
			return;
		}
		List<Long> roleIdList = userRoleEntityList.stream().map(UserRoleEntity::getRoleId).distinct().collect(Collectors.toList());
		List<RoleEntity> roleList = roleMapper.findByIds(roleIdList);
		Map<Long, List<UserRoleEntity>> userRoleMap = userRoleEntityList.stream().collect(Collectors.groupingBy(UserRoleEntity::getUserId));
		Map<Long, List<RoleEntity>> roleMap = roleList.stream().collect(Collectors.groupingBy(RoleEntity::getId));
		for (UserEntity userEntity : dataList) {
			List<UserRoleEntity> userRoleEntities = userRoleMap.get(userEntity.getId());
			if (CollectionUtils.isNotEmpty(userRoleEntities)) {
				List<RoleEntity> roles = Lists.newArrayList();
				for (UserRoleEntity userRoleEntity : userRoleEntities) {
					List<RoleEntity> matchRoleEntities = roleMap.get(userRoleEntity.getRoleId());
					if(CollectionUtils.isNotEmpty(matchRoleEntities)) {
						roles.add(matchRoleEntities.get(0));
					}
				}
				userEntity.setRoles(roles);
			}
		}
	}

	/**
	 * 新增用户
	 *
	 * @param userEntity 用户信息
	 * @return 结果
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void insert(UserEntity userEntity) {
		UserConditionEntity userConditionEntity = new UserConditionEntity();
		userConditionEntity.setUserName(userEntity.getUserName());
		AssertUtil.isTrue(CollectionUtils.isEmpty(userMapper.searchByCondition(userConditionEntity)), "用户名称已存在");
		userConditionEntity = new UserConditionEntity();
		userConditionEntity.setEmail(userEntity.getEmail());
		AssertUtil.isTrue(CollectionUtils.isEmpty(userMapper.searchByCondition(userConditionEntity)), "邮箱已存在");
		if (!StringUtils.hasLength(userEntity.getPassword())) {
			userEntity.setPassword(DEFAULT_PASSWORD);
		}
		userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));
		fillData(userEntity);
		userMapper.insert(userEntity);
		userRoleMapper.deleteByUserId(userEntity.getId());
		List<UserRoleEntity> userRoleEntities = buildUserRoleEntityList(userEntity);
		if (CollectionUtils.isNotEmpty(userRoleEntities)) {
			userRoleMapper.batchInsert(userRoleEntities);
		}
	}

	private List<UserRoleEntity> buildUserRoleEntityList(UserEntity userEntity) {
		if (CollectionUtils.isNotEmpty(userEntity.getRoleList())) {
			return userEntity.getRoleList().stream().map(x -> {
				UserRoleEntity userRoleEntity = new UserRoleEntity();
				userRoleEntity.setUserId(userEntity.getId());
				userRoleEntity.setRoleId(x.getId());
				return userRoleEntity;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}



	/**
	 * 修改用户
	 *
	 * @param userEntity 用户信息
	 * @return 结果
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int update(UserEntity userEntity) {
		fillData(userEntity);
		userRoleMapper.deleteByUserId(userEntity.getId());
		List<UserRoleEntity> userRoleEntities = buildUserRoleEntityList(userEntity);
		if (CollectionUtils.isNotEmpty(userRoleEntities)) {
			userRoleMapper.batchInsert(userRoleEntities);
		}
		return userMapper.update(userEntity);
	}

	/**
	 * 批量删除用户对象
	 *
	 * @param ids 系统ID集合
	 * @return 结果
	 */
	public int deleteByIds(List<Long> ids) {
		List<UserEntity> entities = userMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "用户已被删除");

		UserEntity entity = new UserEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return userMapper.deleteByIds(ids, entity);
	}

	public CaptchaEntity getCode() {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
		// 几位数运算，默认是两位
		captcha.setLen(2);
		// 获取运算的结果
		String result = "";
		try {
			result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
		} catch (Exception e) {
			result = captcha.text();
		}
		String uuid = "C" + IdUtil.simpleUUID();
		// 保存
		redisUtil.set(getCaptchaKey(uuid), result, captchaExpireSecond);
		// 保存
		return new CaptchaEntity(uuid, captcha.toBase64());
	}

	private String getCaptchaKey(String uuid) {
		return String.format("%s%s", CAPTCHA_PREFIX, uuid);
	}

	/**
	 * 批量重置用户密码
	 * @param ids 用户ID
	 * @return
	 */
	public int resetPwd(List<Long> ids) {
		List<UserEntity> userEntities = userMapper.findByIds(ids);
		AssertUtil.notEmpty(userEntities, "用户不存在");

		for (UserEntity userEntity : userEntities) {
			userEntity.setPassword(passwordUtil.encode(DEFAULT_PASSWORD));
			FillUserUtil.fillUpdateUserInfo(userEntity);
		}
		return userMapper.updateForBatch(userEntities);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return userMapper;
	}

}
