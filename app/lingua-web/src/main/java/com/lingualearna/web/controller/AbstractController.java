package com.lingualearna.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.lingualearna.web.security.User;
import com.lingualearna.web.service.UserService;

public abstract class AbstractController {

    protected User getCurrentUser(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = getUserService().getUserByUsername(userDetails.getUsername());
        return user;
    }

    protected abstract UserService getUserService();
}