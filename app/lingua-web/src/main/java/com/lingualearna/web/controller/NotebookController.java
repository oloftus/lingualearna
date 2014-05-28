package com.lingualearna.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.security.User;
import com.lingualearna.web.service.NotebookService;
import com.lingualearna.web.service.UserService;

@Controller
@RequestMapping("/api/notebook")
public class NotebookController extends AbstractController {

    @Autowired
    UserService userService;

    @Autowired
    private NotebookService notebookService;

    @RequestMapping(value = "/notebooksPages", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Notebook> getNotebooks(Authentication authentication) {

        User user = getCurrentUser(userService, authentication);
        List<Notebook> notebooks = notebookService.getAllNotebooksByUser(user.getUserId());
        return notebooks;
    }
}
