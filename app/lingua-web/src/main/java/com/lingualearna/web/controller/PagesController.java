package com.lingualearna.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagesController {

	@RequestMapping(value = "/translate")
	public ModelAndView translateString(HttpServletRequest request, HttpServletResponse response) throws Exception {

	}
}
