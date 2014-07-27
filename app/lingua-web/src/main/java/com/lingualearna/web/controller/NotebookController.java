package com.lingualearna.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.exceptions.ResourceNotFoundException;
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

    public static final String PAGE_NOT_FOUND = "Page not found";

    @Autowired
    private UserService userService;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private ControllerModelMapper<PageModel, Page> pageMapper;

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
        pageMapper.copyPropertiesFromModel(incomingPage, pageEntity, Page.PAGE_ID_FIELD, Page.POSITION_FIELD);
        pageEntity.setNotebook(notebook);
        notebookService.createPage(pageEntity);

        PageModel outgoingPage = new PageModel();
        pageMapper.copyPropertiesFromEntity(pageEntity, outgoingPage);
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

    @RequestMapping(value = "/page/{pageId}", produces = "application/json", consumes = "application/json",
            method = RequestMethod.PUT)
    @ResponseBody
    public PageModel updatePage(@PathVariable int pageId, @RequestBody @Valid PageModel incomingPage)
            throws ValidationException {

        Page pageEntity = notebookService.getPageById(pageId);
        if (pageEntity == null) {
            throw new ResourceNotFoundException(PAGE_NOT_FOUND);
        }

        int oldPosition = pageEntity.getPosition();

        Notebook notebook = notebookService.getNotebookById(incomingPage.getNotebookId());
        pageMapper.copyPropertiesFromModel(incomingPage, pageEntity, Page.PAGE_ID_FIELD);
        pageEntity.setNotebook(notebook);

        if (oldPosition != pageEntity.getPosition()) {
            notebookService.updatePageWithPosition(pageEntity, oldPosition);
        }
        else {
            notebookService.updatePage(pageEntity);
        }

        PageModel outgoingPage = new PageModel();
        pageMapper.copyPropertiesFromEntity(pageEntity, outgoingPage);
        outgoingPage.setNotebookId(notebook.getNotebookId());

        return outgoingPage;
    }
}
