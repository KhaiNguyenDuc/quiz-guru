package com.khai.quizguru.security;

import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.model.user.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;

    private String password;


    private List<GrantedAuthority> authorities;

    public UserPrincipal(String id, String username, String password,
                         List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;

        this.password = password;

        if (authorities == null) {
            this.authorities = null;
        } else {
            this.authorities = new ArrayList<>(authorities);
        }

    }

    public static UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role r : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+r.getName()));
        }
        return new UserPrincipal(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : new ArrayList<>(this.authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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


    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }





}