package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(MockitoJUnitRunner.class)
public class NotebookServiceTest {

    private static final String DUPLICATE_NOTEBOOK_ERROR_KEY = "notebook.duplicateNotebook";
    private static final String DUPLICATE_NOTEBOOK_ERROR_MSG = "nonUniqueName";
    private static final String NOTEBOOK_NAME = "notebook";
    private static final String NOTEBOOK_NAME_FIELD = "name";
    private static final int USER_ID = 1;
    private static final int PAGE_ID = 1;

    @Mock
    private NotebookDao dao;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Notebook> violation;

    @Mock
    private Notebook notebook;

    private Set<ConstraintViolation<Notebook>> violations = new HashSet<>();
    private ConstraintViolationException violationException;
    private ValidationException validationException;

    @InjectMocks
    private NotebookService service;

    private void givenThePassedInNotebookFailsValidation() {

        Notebook anyNotebook = any(Notebook.class);
        when(validator.validate(anyNotebook)).thenReturn(violations);
    }

    private void givenThereIsAnExistingNotebook() {

        when(dao.getCountOfNotebooksWithName(NOTEBOOK_NAME)).thenReturn((long) 1);
    }

    @Before
    public void setup() {

        violations.add(violation);
        when(localizationService.lookupLocalizedString(DUPLICATE_NOTEBOOK_ERROR_KEY)).thenReturn(
                DUPLICATE_NOTEBOOK_ERROR_MSG);
        when(notebook.getName()).thenReturn(NOTEBOOK_NAME);
    }

    @Test
    public void testCreateNotebookPersistsValidNotebook() throws ValidationException {

        whenICallCreateNotebookWithAValidNotebook();
        thenTheNotebookGetsPersisted();
    }

    @Test
    public void testCreateNotebookRejectsAnInvalidNotebook() throws ValidationException {

        givenThePassedInNotebookFailsValidation();
        whenICallCreateNotebookWithThatNotebook();
        thenAnExceptionIsThrownWithValidationInformation();
    }

    @Test
    public void testCreateNotebookRejectsNotebookWithNonUniqueName() throws ValidationException {

        givenThereIsAnExistingNotebook();
        whenICallCreateNotebookWithANotebookOfDuplicateName();
        thenTheExceptionThrownIsCorrect();
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

    private void thenAnExceptionIsThrownWithValidationInformation() {

        assertEquals(violations, violationException.getConstraintViolations());
    }

    private void thenTheExceptionThrownIsCorrect() {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        fieldErrors.put(NOTEBOOK_NAME_FIELD, Lists.newArrayList(DUPLICATE_NOTEBOOK_ERROR_MSG));

        assertEquals(fieldErrors, validationException.getFieldErrors());
        assertEquals(Lists.newArrayList(), validationException.getGlobalErrors());
    }

    private void thenTheNotebookGetsPersisted() {

        verify(dao).persist(notebook);
    }

    private void thenTheServiceDelegatesGetAllNotebooksByUserToTheDao() {

        verify(dao).getAllNotebooksByUser(USER_ID);
    }

    private void thenTheServiceDelegatesGetPageByIdToTheDao() {

        verify(dao).getPageById(USER_ID);
    }

    private void whenICallCreateNotebookWithANotebookOfDuplicateName() {

        try {
            whenICallCreateNotebookWithAValidNotebook();
        }
        catch (ValidationException e) {
            validationException = e;
        }
    }

    private void whenICallCreateNotebookWithAValidNotebook() throws ValidationException {

        service.createNotebook(notebook);
    }

    private void whenICallCreateNotebookWithThatNotebook() throws ValidationException {

        try {
            whenICallCreateNotebookWithAValidNotebook();
        }
        catch (ConstraintViolationException e) {
            violationException = e;
        }
    }

    private void whenICallGetAllNotebooksByUser() {

        service.getAllNotebooksByUser(USER_ID);
    }

    private void whenICallGetPageById() {

        service.getPageById(PAGE_ID);
    }
}
