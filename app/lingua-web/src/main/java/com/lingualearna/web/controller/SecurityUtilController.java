package com.lingualearna.web.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/security")
public class SecurityUtilController {

    private static final String CSRF_TOKEN = "_csrf";

    @Value("${security.csrfSalt}")
    private String csrfSalt;

    private void assertUserPermitted(Principal principal, String sentSecret) {

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();

        String correctSecret = hashUsernamePassword(userDetails.getUsername(), userDetails.getPassword());

        if (!sentSecret.equals(correctSecret)) {
            throw new AccessDeniedException("Incorrect secret");
        }
    }

    @RequestMapping(value = "/csrfToken/{secret}", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String getCsrfToken(HttpServletRequest request, Principal principal, @PathVariable String secret) {

        assertUserPermitted(principal, secret);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN);
        return csrfToken.getToken();
    }

    private String hashUsernamePassword(String username, String password) {

        return DigestUtils.sha1Hex(username + password + csrfSalt);
    }
}
