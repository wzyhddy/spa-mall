package com.net.sparrow.service.sys;

import cn.hutool.core.util.IdUtil;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.auth.AuthUserEntity;
import com.net.sparrow.entity.auth.CaptchaEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.auth.TokenEntity;
import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.entity.sys.UserRoleEntity;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.TokenHelper;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.sys.UserMapper;
import com.net.sparrow.mapper.sys.UserRoleMapper;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
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
	private PasswordUtil passwordUtil;

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
			String token = tokenHelper.generateToken(jwtUserEntity);
			redisUtil.del(getCaptchaKey(authUserEntity.getUuid()));
			List<String> roles = jwtUserEntity.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
			return new TokenEntity(authUserEntity.getUsername(), token, roles);
		} catch (Exception e) {
			log.info("登录失败：", e);
			throw new BusinessException(HttpStatus.FORBIDDEN.value(), " 用户名或密码错误");
		}

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
		return ResponsePageEntity.build(userConditionEntity, count, dataList);
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

	/**
	 * 修改用户
	 *
	 * @param userEntity 用户信息
	 * @return 结果
	 */
	public int update(UserEntity userEntity) {
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

	@Override
	protected BaseMapper getBaseMapper() {
		return userMapper;
	}

}
