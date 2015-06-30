package com.lingualearna.web.shared;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppPagesController {

    @RequestMapping("/notebooks")
    public String notebooksPage() {

        return "notebooks";
    }

    @RequestMapping()
    public String rootPage() {

        return "redirect:/app/notebooks";
    }
}
