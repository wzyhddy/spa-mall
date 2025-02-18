package com.net.sparrow.service.user;

import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sparrow
 * @Description: 实现UserDetailService中的loadByUserName方法
 * @DateTime: 2025/2/18 8:14
 **/
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordUtil passwordUtil;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		String encode = passwordUtil.encode("123456");
		return new JwtUserEntity(123L, username, encode, authorities);
	}
}
