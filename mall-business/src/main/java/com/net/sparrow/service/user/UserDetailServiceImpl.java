package com.net.sparrow.service.user;

import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.entity.sys.MenuEntity;
import com.net.sparrow.entity.sys.RoleEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.mapper.sys.MenuMapper;
import com.net.sparrow.mapper.sys.RoleMapper;
import com.net.sparrow.mapper.sys.UserMapper;
import com.net.sparrow.mapper.sys.UserRoleMapper;
import com.net.sparrow.util.PasswordUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Sparrow
 * @Description: 实现UserDetailService中的loadByUserName方法
 * @DateTime: 2025/2/18 8:14
 **/
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordUtil passwordUtil;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;


	@Autowired
	private MenuMapper menuMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userMapper.findByUserName(username);
		if(Objects.isNull(userEntity)){
			return null;
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		fillUserAuthority(userEntity, authorities);
		List<String> roles = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
		JwtUserEntity jwtUserEntity = new JwtUserEntity(userEntity.getId(), username, userEntity.getPassword(), authorities, roles);
		return jwtUserEntity;
	}

	private void fillUserAuthority(UserEntity userEntity, List<SimpleGrantedAuthority> authorities) {
		List<RoleEntity> roleEntities = roleMapper.findRoleByUserId(userEntity.getId());
		if(CollectionUtils.isEmpty(roleEntities)){
			return;
		}

		Set<String> permissionSet = roleEntities.stream().filter(x -> StringUtils.hasLength(x.getPermission())).map(RoleEntity::getPermission).collect(Collectors.toSet());
		fillRoleMenu(roleEntities, permissionSet);
		if (CollectionUtils.isNotEmpty(permissionSet)) {
			authorities.addAll(permissionSet.stream().map(x->new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
		}
	}

	private void fillRoleMenu(List<RoleEntity> roleEntities, Set<String> permissionSet) {
		List<Long> roleIdList = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList());
		List<MenuEntity> menuList = menuMapper.findMenuByRoleIdList(roleIdList);
		if(CollectionUtils.isEmpty(menuList)){
			return;
		}
		for (MenuEntity menuEntity : menuList) {
			if (StringUtils.hasLength(menuEntity.getPermission())) {
				Set<String> menuPermissionSet = Arrays.stream(menuEntity.getPermission().split(",")).collect(Collectors.toSet());
				permissionSet.addAll(menuPermissionSet);
			}
		}

	}
}
