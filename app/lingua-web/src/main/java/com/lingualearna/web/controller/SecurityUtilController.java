package com.lingualearna.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/security")
public class SecurityUtilController {

    private static final String CSRF_TOKEN = "_csrf";

    /*
     * TODO: This should accept a 'session' token not available in the cookie to
     * avoid XSS attacks, e.g. MD5(username, password, salt)
     */
    @RequestMapping(value = "/csrfToken", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String getCsrfToken(HttpServletRequest request) {

        CsrfToken token = (CsrfToken) request.getAttribute(CSRF_TOKEN);
        return token.getToken();
    }
}
