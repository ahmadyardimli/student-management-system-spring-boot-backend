package com.ahmadyardimli.studentmanagementsystem.security;

import com.ahmadyardimli.studentmanagementsystem.common.CommonUser;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {
    private int id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private JwtUserDetails(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static JwtUserDetails create(CommonUser user) {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        if (user instanceof User){
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (user instanceof Admin){
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new JwtUserDetails(user.getId(), user.getUsername(), user.getPasswordHash(), authoritiesList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
