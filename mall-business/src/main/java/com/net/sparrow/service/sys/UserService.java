package com.net.sparrow.service.sys;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.net.sparrow.dto.web.CityDTO;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.auth.AuthUserEntity;
import com.net.sparrow.entity.auth.CaptchaEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.auth.TokenEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.entity.email.RemoteLoginEmailEntity;
import com.net.sparrow.entity.sys.DeptConditionEntity;
import com.net.sparrow.entity.sys.DeptEntity;
import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.entity.sys.UserRoleEntity;
import com.net.sparrow.enums.EmailTypeEnum;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.enums.TaskTypeEnum;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.GeoIpHelper;
import com.net.sparrow.helper.TokenHelper;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.mapper.sys.DeptMapper;
import com.net.sparrow.mapper.sys.UserMapper;
import com.net.sparrow.mapper.sys.UserRoleMapper;
import com.net.sparrow.util.AssertUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private RedisUtil redisUtil;

	@Value("${mall.mgt.captchaExpireSecond:60}")
	private int captchaExpireSecond;

	@Value("${mall.mgt.remoteLoginDiffHour:48}")
	private int remoteLoginDiffHour;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	private GeoIpHelper geoIpHelper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private PasswordUtil passwordUtil;
	@Autowired
	private CommonTaskMapper commonTaskMapper;
	@Autowired
	private UserDetailsService userDetailsService;

	public TokenEntity login(AuthUserEntity authUserEntity) {
		String code = redisUtil.get(getCaptchaKey(authUserEntity.getUuid()));
		AssertUtil.hasLength(code, "该验证码不存在或者已失效");
		AssertUtil.isTrue(code.trim().equals(authUserEntity.getCode().trim()), "验证码错误");
		try {
			String decodeRsaPassword = passwordUtil.decodeRsaPassword(authUserEntity);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUserEntity.getUsername(), decodeRsaPassword);
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//这里就是 Authentication 内部保存的 UserDetail 对象
			//getPrincipal: 获取用户身份信息，在未认证的情况下获取到的是用户名，在已认证的情况下获取到的是 UserDetails。
			//getDetails: 获取用户的额外信息，（这部分信息可以是我们的用户表中的信息）。
			// getAuthorities: 获取用户权限，一般情况下获取到的是用户的角色信息。
			//getCredentials: 获取证明用户认证的信息，通常情况下获取到的是密码等信息。
			JwtUserEntity jwtUserEntity = (JwtUserEntity) (authentication.getPrincipal());
			UserEntity userEntity = userMapper.findByUserName(jwtUserEntity.getUsername());
			AssertUtil.notNull(userEntity, "该用户不存在");
			//获取当前用户的IP
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
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
		} catch (Exception e) {
			log.info("登录失败：", e);
			throw new BusinessException(HttpStatus.FORBIDDEN.value(), " 用户名或密码错误");
		}

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
		int count = userMapper.searchCount(userConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(userConditionEntity);
		}
		List<UserEntity> dataList = userMapper.searchByCondition(userConditionEntity);
		fillData(dataList);
		return ResponsePageEntity.build(userConditionEntity, count, dataList);
	}

	private void fillData(List<UserEntity> dataList) {
		if (CollectionUtils.isEmpty(dataList)) {
			return;
		}
		List<Long> deptIdList = dataList.stream().filter(x -> Objects.nonNull(x.getDeptId())).map(UserEntity::getDeptId).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(deptIdList)) {
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
			List<DeptEntity> entities = deptMap.get(userEntity.getDeptId());
			if (CollectionUtils.isNotEmpty(entities)) {
				userEntity.setDeptName(entities.get(0).getName());
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
		userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));
		userMapper.insert(userEntity);

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

	/**
	 * 修改用户
	 *
	 * @param userEntity 用户信息
	 * @return 结果
	 */
	public int update(UserEntity userEntity) {
		return userMapper.update(userEntity);
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

	@Override
	protected BaseMapper getBaseMapper() {
		return userMapper;
	}

}
