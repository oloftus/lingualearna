package com.lingualearna.web.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lingualearna.web.service.UserService;

public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.lingualearna.web.security.User userEntity = userService.getUserByUsername(username);

        if (userEntity == null || !userEntity.getEnabled()) {
            throw new UsernameNotFoundException("No such user");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().toString()));
        User user = new User(userEntity.getEmailAddress(), userEntity.getPassword(), authorities);

        return user;
    }
}
