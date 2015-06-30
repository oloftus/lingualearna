package com.lingualearna.web.shared.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.security.users.UserService;

public abstract class AbstractController {

    protected abstract UserService getUserService();

    protected AppUser getCurrentUser(Authentication authentication) {

        UserDetails userDetails = getUserDetails(authentication);
        return getUserService().getUserByUsername(userDetails.getUsername());
    }

    protected String getCurrentUserUsername(Authentication authentication) {

        UserDetails userDetails = getUserDetails(authentication);
        return userDetails.getUsername();
    }

    private UserDetails getUserDetails(Authentication authentication) {

        return (UserDetails) authentication.getPrincipal();
    }
}
