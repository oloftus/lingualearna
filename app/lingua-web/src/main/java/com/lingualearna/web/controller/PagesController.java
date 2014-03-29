package com.lingualearna.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/app")
public class PagesController {

	@RequestMapping("/login")
	public String loginPage() throws Exception {

		return "login";
	}

	@RequestMapping("/translate")
	public String translatePage() throws Exception {

		return "translate";
	}
}
