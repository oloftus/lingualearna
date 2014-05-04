package com.lingualearna.web.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/app")
public class PagesController {

    @RequestMapping("/login")
    public String loginPage() throws Exception {

        return "login";
    }

    @RequestMapping("/loginSuccess")
    public String loginSuccessPage() throws Exception {

        return "loginSuccess";
    }

    @RequestMapping(value = "/api/ping", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String ping() throws Exception {

        return new Date().toString();
    }
}
