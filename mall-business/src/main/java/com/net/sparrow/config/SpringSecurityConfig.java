package com.net.sparrow.config;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.util.SpringBeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Sparrow
 * @Description: SpringSecurityConfig
 * @DateTime: 2025/2/17 22:16
 **/
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		SpringBeanUtil.setApplicationContext(applicationContext);
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		// 去除 ROLE_ 前缀
		return new GrantedAuthorityDefaults("");
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		// 密码加密方式
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
		Set<String> noLoginUrls = new HashSet<>();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
			HandlerMethod handlerMethod = infoEntry.getValue();
			NoLogin noLogin = handlerMethod.getMethodAnnotation(NoLogin.class);
			if (null != noLogin) {
				noLoginUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
			}
		}
		return httpSecurity
				// 禁用 CSRF
				.csrf().disable()
				// 授权异常
				.exceptionHandling()

				.and()
				.headers()
				.frameOptions()
				.disable()

				// 不创建会话
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.authorizeRequests()
				// 静态资源等等
				.antMatchers(
						HttpMethod.GET,
						"/*.html",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js",
						"/websocket/**"
				).permitAll()
				// swagger 文档
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/*/api-docs").permitAll()
				.antMatchers("/avatar/**").permitAll()
				.antMatchers("/druid/**").permitAll()
				// 放行OPTIONS请求
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers(noLoginUrls.toArray(new String[0])).permitAll()
				// 所有请求都需要认证
				.anyRequest().authenticated()
				.and().apply(new JwtTokenConfigurer())
				.and()
				.build();
	}
}
