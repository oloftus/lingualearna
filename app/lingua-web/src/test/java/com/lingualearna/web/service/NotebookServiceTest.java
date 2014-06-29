package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.languages.LanguageNamesValidator;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.User;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(MockitoJUnitRunner.class)
public class NotebookServiceTest {

    private static final int CURRENT_MAX_POSITION = 1;
    private static final Locale FOREIGN_LANG = Locale.forLanguageTag("en");
    private static final Locale LOCAL_LANG = Locale.forLanguageTag("fr");
    private static final String DUPLICATE_NOTEBOOK_ERROR_KEY = "notebook.duplicateNotebook";
    private static final String DUPLICATE_NOTEBOOK_ERROR_MSG = "nonUniqueName";
    private static final String NOTEBOOK_NAME = "notebook";
    private static final int USER_ID = 1;
    private static final int PAGE_ID = 2;
    private static final int NOTEBOOK_ID = 3;

    @Mock
    private NotebookDao dao;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private LanguageNamesValidator langNamesValidator;

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Notebook> notebookViolation;

    @Mock
    private ConstraintViolation<Page> pageViolation;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Notebook notebook;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Page page;

    @Mock
    private User user;

    private Set<ConstraintViolation<Notebook>> notebookviolations = new HashSet<>();
    private Set<ConstraintViolation<Page>> pageViolations = new HashSet<>();
    private ConstraintViolationException violationException;

    @InjectMocks
    private NotebookService service;

    private void andThePageIsPersisted() {

        verify(dao).persist(page);
    }

    @SuppressWarnings("unchecked")
    private void givenThereAreNoExistingPages() {

        when(dao.doUntypedQueryAsListWithParams(any(String.class), (Pair<String, ? extends Object>[]) anyVararg()))
                .thenReturn(Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    private void givenThereIsAnExistingPage() {

        List<Object> resultsList = new ArrayList<>();
        resultsList.add(CURRENT_MAX_POSITION);
        when(dao.doUntypedQueryAsListWithParams(any(String.class), (Pair<String, ? extends Object>[]) anyVararg()))
                .thenReturn(resultsList);
    }

    @Before
    public void setup() {

        when(localizationService.lookupLocalizedString(DUPLICATE_NOTEBOOK_ERROR_KEY)).thenReturn(
                DUPLICATE_NOTEBOOK_ERROR_MSG);

        when(notebook.getName()).thenReturn(NOTEBOOK_NAME);
        when(notebook.getForeignLang()).thenReturn(FOREIGN_LANG);
        when(notebook.getLocalLang()).thenReturn(LOCAL_LANG);

        when(page.getNotebook().getOwner()).thenReturn(user);
    }

    private void setupInvalidNotebook() {

        notebookviolations.add(notebookViolation);
        when(validator.validate(any(Notebook.class))).thenReturn(notebookviolations);
    }

    private void setupInvalidPage() {

        pageViolations.add(pageViolation);
        when(validator.validate(any(Page.class))).thenReturn(pageViolations);
    }

    private void setupNotebookWithInvalidLanguages() throws ValidationException {

        doThrow(new ValidationException()).when(langNamesValidator).validateLanguageNames((String) anyVararg());
    }

    @Test
    public void testCreateNotebookPersistsValidNotebook() throws ValidationException {

        whenICallCreateNotebook();
        thenTheNotebookGetsPersisted();
    }

    @Test
    public void testCreateNotebookRejectsAnInvalidNotebook() throws ValidationException {

        whenICallCreateNotebookWithAnInvalidNotebook();
        thenTheExceptionThrownContainsInvalidNotebookMessage();
    }

    @Test(expected = ValidationException.class)
    public void testCreateNotebookRejectsInvalidLanguages() throws ValidationException {

        whenICallCreateNotebookWithANotebookWithInvalidLanguagesThenAnExceptionIsThrown();
    }

    @Test
    public void testCreatePageCorrectlySetsFirstPagePosition() throws ValidationException {

        givenThereAreNoExistingPages();
        whenICallCreatePage();
        thenThePagePositionIsSetToOne();
    }

    @Test
    public void testCreatePageFunctionsForAValidPage() throws ValidationException {

        givenThereIsAnExistingPage();
        whenICallCreatePage();
        thenThePagePositionIsSet();
        andThePageIsPersisted();
    }

    @Test
    public void testCreatePageRejectsAnValidPage() throws ValidationException {

        whenICallCreatePageWithAnInvalidPage();
        thenTheExceptionThrownContainsInvalidPageMessage();
    }

    @Test
    public void testGetAllNotebooksByUserDelegatesToDao() {

        whenICallGetAllNotebooksByUser();
        thenTheServiceDelegatesGetAllNotebooksByUserToTheDao();
    }

    @Test
    public void testGetNotebookByIdDelegatesToDao() {

        whenICallGetNotebookById();
        thenTheServiceDelegatesGetNotebookByIdToTheDao();
    }

    @Test
    public void testGetPageByIdDelegatesToDao() {

        whenICallGetPageById();
        thenTheServiceDelegatesGetPageByIdToTheDao();
    }

    private void thenTheExceptionThrownContainsInvalidNotebookMessage() {

        assertEquals(notebookviolations, violationException.getConstraintViolations());
    }

    private void thenTheExceptionThrownContainsInvalidPageMessage() {

        assertEquals(pageViolations, violationException.getConstraintViolations());
    }

    private void thenTheNotebookGetsPersisted() {

        verify(dao).persist(notebook);
    }

    private void thenThePagePositionIsSet() {

        verify(page).setPosition(CURRENT_MAX_POSITION + 1);
    }

    private void thenThePagePositionIsSetToOne() {

        verify(page).setPosition(1);
    }

    private void thenTheServiceDelegatesGetAllNotebooksByUserToTheDao() {

        verify(dao).getAllNotebooksByUser(USER_ID);
    }

    private void thenTheServiceDelegatesGetNotebookByIdToTheDao() {

        verify(dao).find(Notebook.class, NOTEBOOK_ID);
    }

    private void thenTheServiceDelegatesGetPageByIdToTheDao() {

        verify(dao).getPageById(PAGE_ID);
    }

    private void whenICallCreateNotebook() throws ValidationException {

        service.createNotebook(notebook);
    }

    private void whenICallCreateNotebookWithAnInvalidNotebook() throws ValidationException {

        setupInvalidNotebook();

        try {
            whenICallCreateNotebook();
        }
        catch (ConstraintViolationException e) {
            violationException = e;
        }
    }

    private void whenICallCreateNotebookWithANotebookWithInvalidLanguagesThenAnExceptionIsThrown()
            throws ValidationException {

        setupNotebookWithInvalidLanguages();
        whenICallCreateNotebook();
    }

    private void whenICallCreatePage() throws ValidationException {

        service.createPage(page);
    }

    private void whenICallCreatePageWithAnInvalidPage() throws ValidationException {

        setupInvalidPage();

        try {
            whenICallCreatePage();
        }
        catch (ConstraintViolationException e) {
            violationException = e;
        }
    }

    private void whenICallGetAllNotebooksByUser() {

        service.getAllNotebooksByUser(USER_ID);
    }

    private void whenICallGetNotebookById() {

        service.getNotebookById(NOTEBOOK_ID);
    }

    private void whenICallGetPageById() {

        service.getPageById(PAGE_ID);
    }
}
