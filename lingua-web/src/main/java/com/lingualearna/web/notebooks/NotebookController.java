package com.lingualearna.web.notebooks;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.pages.Page;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractController;

@Controller
@RequestMapping("/api/notebook")
public class NotebookController extends AbstractController {

    private static final int NEW_NOTEBOOK_ID = 0;

    @Autowired
    private UserService userService;

    @Autowired
    private NotebookService notebookService;

    @Override
    protected UserService getUserService() {

        return userService;
    }

    @RequestMapping(produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Notebook createNotebook(Authentication authentication, @RequestBody @Valid Notebook notebook)
    {

        blankOutPrivateValues(notebook);

        String ownerUsername = getCurrentUserUsername(authentication);
        notebookService.createNotebook(notebook, ownerUsername);

        return notebook;
    }

    private void blankOutPrivateValues(Notebook notebook) {

        notebook.setPages(Collections.<Page> emptyList());
        notebook.setNotebookId(NEW_NOTEBOOK_ID);
        notebook.setOwner(null);
    }

    @RequestMapping(value = "/notebooksPages", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Notebook> getNotebooksAndPages(Authentication authentication) {

        String username = getCurrentUserUsername(authentication);
        return notebookService.getAllNotebooksByUser(username);
    }
}
