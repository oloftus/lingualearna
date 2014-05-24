package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
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

import com.lingualearna.web.dao.NotesDao;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.User;

@RunWith(MockitoJUnitRunner.class)
public class NotesServiceTest {

    private static final int NOTE_ID = 1;
    private static final String USERNAME = "username";

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Note> violation;

    @Mock
    private User user;

    @Mock
    private NotesDao notesDao;

    private Note note;
    private Note daoReturnedNote;
    private Note serviceReturnedNote;

    @InjectMocks
    private NotesService notesService = new NotesService();

    private void andTheDaoIsSetOnThePassedInNote() {

        assertEquals(USERNAME, note.getOwnerUsername());
    }

    private void andTheDaoIsSetOnTheReturnedNote() {

        assertEquals(USERNAME, serviceReturnedNote.getOwnerUsername());
    }

    private void andTheNoteIsPersisted() {

        verify(notesDao).persist(note);
    }

    private void givenValidationFails() {

        Set<ConstraintViolation<Note>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(note)).thenReturn(violations);
    }

    @Before
    public void setup() {

        daoReturnedNote = new Note();
        daoReturnedNote.setNoteId(NOTE_ID);

        note = new Note();
        note.setNoteId(NOTE_ID);

        when(notesDao.getNoteOwner(NOTE_ID)).thenReturn(user);
        when(user.getUsername()).thenReturn(USERNAME);

        when(notesDao.findNoLock(NOTE_ID)).thenReturn(daoReturnedNote);
        when(notesDao.merge(note)).thenReturn(daoReturnedNote);
    }

    @Test
    public void testCreateNoteFunctions() {

        whenICallCreateNote();
        thenTheNoteIsValidated();
        andTheNoteIsPersisted();
        andTheDaoIsSetOnThePassedInNote();
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
        andTheDaoIsSetOnTheReturnedNote();
    }

    @Test
    public void testUpdateNoteFunctions() {

        whenICallUpdateNote();
        thenTheNoteIsValidated();
        thenTheNoteIsUpdated();
        andTheDaoIsSetOnTheReturnedNote();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testValidationErrorsCauseException() {

        givenValidationFails();
        whenANoteIsValidatedTheCorrectExceptionIsThrown();
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

    private void whenICallRetrieveNote() {

        serviceReturnedNote = notesService.retrieveNote(NOTE_ID);
    }

    private void whenICallUpdateNote() {

        serviceReturnedNote = notesService.updateNote(note);
    }
}
