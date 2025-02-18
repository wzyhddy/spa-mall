package com.net.sparrow.controller.web;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.entity.auth.AuthUserEntity;
import com.net.sparrow.entity.auth.CaptchaEntity;
import com.net.sparrow.entity.auth.TokenEntity;
import com.net.sparrow.service.sys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: Sparrow
 * @DateTime: 2025/2/17 22:29
 **/
@Api(tags = "web用户操作", description = "web用户接口")
@RestController
@RequestMapping("/v1/web/user")
@Validated
public class WebUserController {

	@Autowired
	private UserService userService;

	/**
	 * 用户登录
	 *
	 * @param authUserEntity 用户实体
	 * @return 影响行数
	 */
	@NoLogin
	@ApiOperation(notes = "用户登录", value = "用户登录")
	@PostMapping("/login")
	public TokenEntity login(@Valid @RequestBody AuthUserEntity authUserEntity) {
		return userService.login(authUserEntity);
	}

	/**
	 * 用户退出登录
	 *
	 * @param request 请求
	 * @return 影响行数
	 */
	@NoLogin
	@ApiOperation(notes = "用户退出登录", value = "用户退出登录")
	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
//		userService.logout(request);
	}

	@NoLogin
	@ApiOperation("获取验证码")
	@GetMapping(value = "/code")
	public CaptchaEntity getCode() {
		return userService.getCode();
	}
}
