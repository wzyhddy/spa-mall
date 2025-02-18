package com.net.sparrow.filter;

import com.net.sparrow.helper.TokenHelper;
import com.net.sparrow.util.SpringBeanUtil;
import com.net.sparrow.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author: Sparrow
 * @Description: token过滤器
 * @DateTime: 2025/2/17 22:49
 **/
public class JwtTokenFilter extends GenericFilterBean {

	/**
	 * 如果没有获取到token,则执行过滤器链返回
	 * 如果获取到token从token中取出username
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String token = TokenUtil.getTokenForAuthorization(httpServletRequest);
		if (Objects.isNull(token)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		TokenHelper tokenHelper = SpringBeanUtil.getBean("tokenHelper");
		String username = tokenHelper.getUsernameFromToken(token);
		if (StringUtils.isNoneBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails detailsFromUsername = tokenHelper.getUserDetailsFromUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(detailsFromUsername, null, detailsFromUsername.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}


}
