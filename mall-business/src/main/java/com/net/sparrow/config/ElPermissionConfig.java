package com.net.sparrow.config;

import com.net.sparrow.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "el")
public class ElPermissionConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    public Boolean valid(String... permissions) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "当前登录状态过期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> elPermissions = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return elPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
        }
        return false;
    }
}
