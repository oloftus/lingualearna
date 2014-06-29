package com.lingualearna.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.controller.model.PageModel;
import com.lingualearna.web.controller.modelmappers.ControllerModelMapper;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.User;
import com.lingualearna.web.service.NotebookService;
import com.lingualearna.web.service.UserService;

@Controller
@RequestMapping("/api/notebook")
public class NotebookController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private ControllerModelMapper<PageModel, Page> pagesMapper;

    @RequestMapping(value = "", produces = "application/json", consumes = "application/json",
            method = RequestMethod.POST)
    @ResponseBody
    public Notebook createNotebook(Authentication authentication, @RequestBody @Valid Notebook notebook)
            throws ValidationException {

        User currentUser = getCurrentUser(userService, authentication);
        notebook.setOwner(currentUser);
        notebookService.createNotebook(notebook);
        return notebook;
    }

    @RequestMapping(value = "/page", produces = "application/json", consumes = "application/json",
            method = RequestMethod.POST)
    @ResponseBody
    public PageModel createPage(Authentication authentication, @RequestBody @Valid PageModel incomingPage)
            throws ValidationException {

        Notebook notebook = notebookService.getNotebookById(incomingPage.getNotebookId());
        Page pageEntity = new Page();
        pagesMapper.copyPropertiesFromModel(incomingPage, pageEntity, Page.PAGE_ID_FIELD, Page.POSITION_FIELD);
        pageEntity.setNotebook(notebook);
        notebookService.createPage(pageEntity);

        PageModel outgoingPage = new PageModel();
        pagesMapper.copyPropertiesFromEntity(pageEntity, outgoingPage);
        outgoingPage.setNotebookId(pageEntity.getNotebook().getNotebookId());

        return outgoingPage;
    }

    @RequestMapping(value = "/notebooksPages", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Notebook> getNotebooks(Authentication authentication) {

        User currentUser = getCurrentUser(userService, authentication);
        List<Notebook> notebooks = notebookService.getAllNotebooksByUser(currentUser.getUserId());
        return notebooks;
    }
}
