package com.net.sparrow.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtUserEntity implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private List<SimpleGrantedAuthority> authorities;

    private List<String> roles;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
