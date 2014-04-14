package com.lingualearna.web.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.notes.Note;

@RunWith(MockitoJUnitRunner.class)
public class NotesServiceTest {

    private static final int NOTE_ID = 1;

    @Mock
    private Validator validator;

    @Mock
    private GenericDao<Note> notesDao;

    @Mock
    private Note note;

    @Mock
    private ConstraintViolation<Note> violation;

    @InjectMocks
    private NotesService notesService = new NotesService();

    private void andTheNoteIsPersisted() {

        verify(notesDao).persist(note);
    }

    private void givenValidationFails() {

        Set<ConstraintViolation<Note>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(note)).thenReturn(violations);
    }

    @Test
    public void testCreateNoteFunctions() {

        whenICallCreateNote();
        thenTheNoteIsValidated();
        andTheNoteIsPersisted();
    }

    @Test
    public void testDeleteNoteFunctions() {

        whenICallDeleteNote();
        thenTheNoteIsDeleted();
    }

    @Test
    public void testInitSetsUpDao() {

        whenICallInit();
        thenTheDaoIsSetup();
    }

    @Test
    public void testRetrieveNoteFunctions() {

        whenICallRetrieveNote();
        thenTheNoteIsRetrieved();
    }

    @Test
    public void testUpdateNoteFunctions() {

        whenICallUpdateNote();
        thenTheNoteIsValidated();
        thenTheNoteIsUpdated();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testValidationErrorsCauseException() {

        givenValidationFails();
        whenANoteIsValidatedTheCorrectExceptionIsThrown();
    }

    private void thenTheDaoIsSetup() {

        verify(notesDao).setEntityType(Note.class);
    }

    private void thenTheNoteIsDeleted() {

        verify(notesDao).delete(NOTE_ID);
    }

    private void thenTheNoteIsRetrieved() {

        verify(notesDao).findNoLock(NOTE_ID);
    }

    private void thenTheNoteIsUpdated() {

        verify(notesDao).merge(note);
    }

    private void thenTheNoteIsValidated() {

        verify(validator).validate(note);
    }

    private void whenANoteIsValidatedTheCorrectExceptionIsThrown() {

        whenICallCreateNote();
    }

    private void whenICallCreateNote() {

        notesService.createNote(note);
    }

    private void whenICallDeleteNote() {

        notesService.deleteNote(NOTE_ID);
    }

    private void whenICallInit() {

        notesService.init();
    }

    private void whenICallRetrieveNote() {

        notesService.retrieveNote(NOTE_ID);
    }

    private void whenICallUpdateNote() {

        notesService.updateNote(note);
    }
}
