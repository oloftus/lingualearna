package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.User;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {

    private static final int NOTE_ID = 1;

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Note> violation;

    @Mock
    private User user;

    @Mock
    private GenericDao<Note> notesDao;

    @Mock
    private Note passedInNote;

    @Mock
    private Note expectedNote;

    private Note returnedNote;
    private boolean deletedSuccess;

    @InjectMocks
    private NoteService notesService = new NoteService();

    private void andTheNoteIsPersisted() {

        verify(notesDao).persist(passedInNote);
    }

    private void andTheNoteIsUpdated() {

        assertEquals(expectedNote, returnedNote);
    }

    private void givenValidationFails() {

        Set<ConstraintViolation<Note>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(passedInNote)).thenReturn(violations);
    }

    @Before
    public void setup() {

        setupNotes();
        setupNotesDao();
    }

    private void setupNotes() {

        when(passedInNote.getNoteId()).thenReturn(NOTE_ID);
        when(expectedNote.getNoteId()).thenReturn(NOTE_ID);
    }

    private void setupNotesDao() {

        when(notesDao.findNoLock(NOTE_ID)).thenReturn(expectedNote);
        when(notesDao.merge(passedInNote)).thenReturn(expectedNote);
        when(notesDao.delete(NOTE_ID)).thenReturn(true);
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
    public void testRetrieveNoteFunctions() {

        whenICallRetrieveNote();
        thenTheNoteIsRetrieved();
    }

    @Test
    public void testUpdateNoteFunctions() {

        whenICallUpdateNote();
        thenTheNoteIsValidated();
        andTheNoteIsUpdated();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testValidationErrorsCauseException() {

        givenValidationFails();
        whenANoteIsValidatedTheCorrectExceptionIsThrown();
    }

    private void thenTheNoteIsDeleted() {

        assertTrue(deletedSuccess);
    }

    private void thenTheNoteIsRetrieved() {

        assertEquals(expectedNote, returnedNote);
    }

    private void thenTheNoteIsValidated() {

        verify(validator).validate(passedInNote);
    }

    private void whenANoteIsValidatedTheCorrectExceptionIsThrown() {

        whenICallCreateNote();
    }

    private void whenICallCreateNote() {

        notesService.createNote(passedInNote);
    }

    private void whenICallDeleteNote() {

        deletedSuccess = notesService.deleteNote(NOTE_ID);
    }

    private void whenICallRetrieveNote() {

        returnedNote = notesService.retrieveNote(NOTE_ID);
    }

    private void whenICallUpdateNote() {

        returnedNote = notesService.updateNote(passedInNote);
    }
}
