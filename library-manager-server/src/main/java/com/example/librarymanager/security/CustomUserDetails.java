package com.example.librarymanager.security;

import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.domain.entity.UserGroupRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final String userId;

    @Getter
    private final String cardNumber;

    @JsonIgnore
    private final String username;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String cardNumber, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Set<UserGroupRole> roles = user.getUserGroup().getUserGroupRoles();
        for (UserGroupRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
        }

        return new CustomUserDetails(user.getId(), null, user.getUsername(), user.getPassword(), authorities);
    }

    public static CustomUserDetails create(Reader reader) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleConstant.ROLE_READER.name()));
        return new CustomUserDetails(null, reader.getCardNumber(), reader.getFullName(), reader.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
