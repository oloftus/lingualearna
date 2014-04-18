package com.lingualearna.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/app")
public class PagesController {

    @RequestMapping("/note")
    public String addNotePage() throws Exception {

        return "addNote";
    }

    @RequestMapping("/note/{noteId}")
    public ModelAndView editNotePage(@PathVariable int noteId) throws Exception {

        ModelAndView mav = new ModelAndView("editNote");
        mav.addObject(noteId);
        return mav;
    }

    @RequestMapping("/login")
    public String loginPage() throws Exception {

        return "login";
    }

    @RequestMapping("/translate")
    public String translatePage() throws Exception {

        return "translate";
    }
}
