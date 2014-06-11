package com.lingualearna.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
