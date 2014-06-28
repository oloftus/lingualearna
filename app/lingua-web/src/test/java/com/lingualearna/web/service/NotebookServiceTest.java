package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.languages.LanguageNamesValidator;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(MockitoJUnitRunner.class)
public class NotebookServiceTest {

    private static final Locale FOREIGN_LANG = Locale.forLanguageTag("en");
    private static final Locale LOCAL_LANG = Locale.forLanguageTag("fr");
    private static final String DUPLICATE_NOTEBOOK_ERROR_KEY = "notebook.duplicateNotebook";
    private static final String DUPLICATE_NOTEBOOK_ERROR_MSG = "nonUniqueName";
    private static final String NOTEBOOK_NAME = "notebook";
    private static final String NOTEBOOK_NAME_FIELD = "name";
    private static final int USER_ID = 1;
    private static final int PAGE_ID = 2;

    @Mock
    private NotebookDao dao;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private LanguageNamesValidator langNamesValidator;

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Notebook> violation;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Notebook notebook;

    private Set<ConstraintViolation<Notebook>> violations = new HashSet<>();
    private ConstraintViolationException violationException;
    private ValidationException validationException;

    @InjectMocks
    private NotebookService service;

    @Before
    public void setup() {

        violations.add(violation);

        when(localizationService.lookupLocalizedString(DUPLICATE_NOTEBOOK_ERROR_KEY)).thenReturn(
                DUPLICATE_NOTEBOOK_ERROR_MSG);

        when(notebook.getName()).thenReturn(NOTEBOOK_NAME);
        when(notebook.getForeignLang()).thenReturn(FOREIGN_LANG);
        when(notebook.getLocalLang()).thenReturn(LOCAL_LANG);
    }

    private void setupExistingNotebook() {

        when(dao.getCountOfNotebooksWithName(NOTEBOOK_NAME)).thenReturn((long) 1);
    }

    private void setupInvalidNotebook() {

        when(validator.validate(any(Notebook.class))).thenReturn(violations);
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
    public void testCreateNotebookRejectsNotebookWithDupliateName() throws ValidationException {

        whenICallCreateNotebookWithANotebookOfDuplicateName();
        thenThrownExceptionContainsDuplicateNotebookMessage();
    }

    @Test
    public void testGetAllNotebooksByUserDelegatesToDao() {

        whenICallGetAllNotebooksByUser();
        thenTheServiceDelegatesGetAllNotebooksByUserToTheDao();
    }

    @Test
    public void testGetPageByIdDelegatesToDao() {

        whenICallGetPageById();
        thenTheServiceDelegatesGetPageByIdToTheDao();
    }

    private void thenTheExceptionThrownContainsInvalidNotebookMessage() {

        assertEquals(violations, violationException.getConstraintViolations());
    }

    private void thenTheNotebookGetsPersisted() {

        verify(dao).persist(notebook);
    }

    private void thenTheServiceDelegatesGetAllNotebooksByUserToTheDao() {

        verify(dao).getAllNotebooksByUser(USER_ID);
    }

    private void thenTheServiceDelegatesGetPageByIdToTheDao() {

        verify(dao).getPageById(PAGE_ID);
    }

    private void thenThrownExceptionContainsDuplicateNotebookMessage() {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        fieldErrors.put(NOTEBOOK_NAME_FIELD, Lists.newArrayList(DUPLICATE_NOTEBOOK_ERROR_MSG));

        assertEquals(fieldErrors, validationException.getFieldErrors());
        assertEquals(Lists.newArrayList(), validationException.getGlobalErrors());
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

    private void whenICallCreateNotebookWithANotebookOfDuplicateName() {

        setupExistingNotebook();

        try {
            whenICallCreateNotebook();
        }
        catch (ValidationException e) {
            validationException = e;
        }
    }

    private void whenICallCreateNotebookWithANotebookWithInvalidLanguagesThenAnExceptionIsThrown()
            throws ValidationException {

        setupNotebookWithInvalidLanguages();
        whenICallCreateNotebook();
    }

    private void whenICallGetAllNotebooksByUser() {

        service.getAllNotebooksByUser(USER_ID);
    }

    private void whenICallGetPageById() {

        service.getPageById(PAGE_ID);
    }
}
