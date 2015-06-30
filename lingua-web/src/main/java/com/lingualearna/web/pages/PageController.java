package com.lingualearna.web.pages;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.notebooks.Notebook;
import com.lingualearna.web.notebooks.NotebookService;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractController;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;

@Controller
@RequestMapping("/api/notebook/page")
public class PageController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ObjectMapper<PageModel, Page> pageMapper;

    @Override
    protected UserService getUserService() {

        return userService;
    }

    @RequestMapping(produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public PageModel createPage(@RequestBody @Valid PageModel incomingPage) {

        Page pageEntity = new Page();
        pageMapper.copyPropertiesLtr(incomingPage, pageEntity, Page.PAGE_ID_FIELD, Page.POSITION_FIELD,
                Page.NOTES_FIELD);
        pageService.createPage(pageEntity, incomingPage.getNotebookId());

        return convertPageToPageModel(pageEntity);
    }

    @RequestMapping(value = "/{pageId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deletePage(@PathVariable int pageId) {

        pageService.deletePage(pageId);
    }

    @RequestMapping(value = "/{pageId}", produces = "application/json", consumes = "application/json",
            method = RequestMethod.PUT)
    @ResponseBody
    public PageModel updatePage(@PathVariable int pageId, @RequestBody @Valid PageModel incomingPage)
    {

        Page pageShell = new Page();
        pageMapper.copyPropertiesLtr(incomingPage, pageShell, Page.PAGE_ID_FIELD, Page.NOTES_FIELD);
        pageShell.setPageId(pageId);
        Notebook notebookShell = new Notebook();
        notebookShell.setNotebookId(incomingPage.getNotebookId());
        pageShell.setNotebook(notebookShell);

        Page updatedPage = pageService.updatePage(pageShell);

        return convertPageToPageModel(updatedPage);
    }

    private PageModel convertPageToPageModel(Page pageEntity) {

        PageModel outgoingPage = new PageModel();
        pageMapper.copyPropertiesRtl(pageEntity, outgoingPage);
        outgoingPage.setNotebookId(pageEntity.getNotebook().getNotebookId());

        return outgoingPage;
    }
}
