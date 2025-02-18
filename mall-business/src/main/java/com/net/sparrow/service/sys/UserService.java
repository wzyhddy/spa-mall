package com.net.sparrow.service.sys;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.IdUtil;
import com.net.sparrow.entity.auth.AuthUserEntity;
import com.net.sparrow.entity.auth.CaptchaEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.auth.TokenEntity;
import com.net.sparrow.helper.TokenHelper;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.PasswordUtil;
import com.net.sparrow.util.RedisUtil;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.UserMapper;
import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 用户 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
public class UserService extends com.net.sparrow.service.BaseService< UserEntity,  UserConditionEntity> {

	private static final String CAPTCHA_PREFIX = "captcha:";

	@Autowired
	private UserMapper userMapper;

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

	public TokenEntity login(AuthUserEntity authUserEntity) {
		String decodeRsaPassword = passwordUtil.decodeRsaPassword(authUserEntity);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUserEntity.getUsername(), decodeRsaPassword);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		JwtUserEntity jwtUserEntity = (JwtUserEntity) (authentication.getPrincipal());
		String token = tokenHelper.generateToken(jwtUserEntity);
		List<String> roles = jwtUserEntity.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
		return new TokenEntity(authUserEntity.getUsername(), token, roles);
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
	public int insert(UserEntity userEntity) {
	    return userMapper.insert(userEntity);
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
