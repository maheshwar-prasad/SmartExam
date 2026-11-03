package com.smartexam.security;

import com.smartexam.permission.entity.Permission;
import com.smartexam.role.entity.Role;
import com.smartexam.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {

        this.user = user;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        Role role = user.getRole();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_" + role.getName())
        );

        if (role.getPermissions() != null) {

            for (Permission permission : role.getPermissions()) {

                authorities.add(
                        new SimpleGrantedAuthority(permission.getName())
                );

            }

        }

        return authorities;

    }

    @Override
    public String getPassword() {

        return user.getPassword();

    }

    @Override
    public String getUsername() {

        return user.getEmail();

    }

    @Override
    public boolean isAccountNonExpired() {

        return user.getAccountNonExpired();

    }

    @Override
    public boolean isAccountNonLocked() {

        return user.getAccountNonLocked();

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return user.getCredentialsNonExpired();

    }

    @Override
    public boolean isEnabled() {

        return user.getEnabled();

    }

}