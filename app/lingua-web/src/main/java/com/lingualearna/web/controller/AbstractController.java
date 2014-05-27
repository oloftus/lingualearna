package com.lingualearna.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.lingualearna.web.security.User;
import com.lingualearna.web.service.UserService;

public class AbstractController {

    protected User getCurrentUser(UserService userService, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        return user;
    }
}