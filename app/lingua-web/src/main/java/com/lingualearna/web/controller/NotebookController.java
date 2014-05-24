package com.lingualearna.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.model.LastUsedModel;
import com.lingualearna.web.notes.LastUsed;
import com.lingualearna.web.security.User;
import com.lingualearna.web.service.UserService;

@Controller
@RequestMapping("/api/notebook")
public class NotebookController {

    @Autowired
    private UserService userService;

    private void copyProperties(LastUsedModel lastUsedModel, LastUsed lastUsed) {

        lastUsedModel.setNotebookId(lastUsed.getNotebook().getNotebookId());
        lastUsedModel.setPageId(lastUsed.getPage().getPageId());
    }

    @RequestMapping(value = "/lastUsed", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public LastUsedModel getLastUsed(Authentication authentication) {

        User user = getUser(authentication);
        LastUsed lastUsed = user.getLastUsed();
        LastUsedModel lastUsedModel = new LastUsedModel();

        copyProperties(lastUsedModel, lastUsed);

        return lastUsedModel;
    }

    private User getUser(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        return user;
    }
}
