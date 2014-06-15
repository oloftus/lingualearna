package com.lingualearna.web.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/app")
public class AppPagesController {

    @RequestMapping("/login")
    public String loginPage() throws Exception {

        return "login";
    }

    @RequestMapping("/loginSuccess")
    public String loginSuccessPage() throws Exception {

        return "loginSuccess";
    }

    @RequestMapping("/notebook")
    public String noteboookPage() throws Exception {

        return "notebook";
    }

    @RequestMapping(value = "/ping", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String ping(HttpServletRequest request, Principal principal) {

        return new DateTime().toString();
    }
}
