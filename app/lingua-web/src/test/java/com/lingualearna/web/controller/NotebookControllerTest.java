package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.controller.model.PageModel;
import com.lingualearna.web.controller.modelmappers.ControllerModelMapper;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.service.NotebookService;
import com.lingualearna.web.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerTest {

    private static final int NOTEBOOK_ID = 1;

    @Captor
    private ArgumentCaptor<Page> pageArg;

    @Captor
    private ArgumentCaptor<PageModel> pageModelArg;

    @Mock
    private UserService userService;

    @Mock
    private Notebook notebook;

    @Mock
    private NotebookService notebookService;

    @Mock
    private PageModel incomingPage;

    @Mock
    private Authentication authentication;

    @Mock
    private ControllerModelMapper<PageModel, Page> pagesMapper;

    private Page pageEntity;
    private PageModel pageModel;
    private PageModel returnedPageModel;

    @InjectMocks
    private NotebookController controller;

    private void andTheCreatedPageModelIsReturned() {

        verify(pagesMapper).copyPropertiesFromEntity(eq(pageEntity), pageModelArg.capture());
        pageModel = pageModelArg.getValue();
        assertEquals((Integer) NOTEBOOK_ID, pageModel.getNotebookId());
        assertEquals(pageModel, returnedPageModel);
    }

    private void andThePageIsCreated() throws ValidationException {

        verify(notebookService).createPage(pageEntity);
    }

    @Before
    public void setup() {

        when(notebook.getNotebookId()).thenReturn(NOTEBOOK_ID);
        when(incomingPage.getNotebookId()).thenReturn(NOTEBOOK_ID);
        when(notebookService.getNotebookById(NOTEBOOK_ID)).thenReturn(notebook);
    }

    @Test
    public void testCreatePageFunctions() throws ValidationException {

        whenICallCreatePage();
        thenThePageHasAllPropertiesSetExcludingPageIdAndPosition();
        andThePageIsCreated();
        andTheCreatedPageModelIsReturned();
    }

    private void thenThePageHasAllPropertiesSetExcludingPageIdAndPosition() {

        verify(pagesMapper).copyPropertiesFromModel(eq(incomingPage), pageArg.capture(), eq(Page.PAGE_ID_FIELD),
                eq(Page.POSITION_FIELD));
        pageEntity = pageArg.getValue();
        assertEquals(notebook, pageEntity.getNotebook());
    }

    private void whenICallCreatePage() throws ValidationException {

        returnedPageModel = controller.createPage(authentication, incomingPage);
    }

}
