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
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.notes.LastUsed;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.User;
import com.lingualearna.web.service.NoteService;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {

    private static final int NOTE_ID = 1;
    private static final int PAGE_ID = 2;
    private static final int NOTEBOOK_ID = 3;

    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<Note> violation;

    @Mock
    private User user;

    @Mock
    private GenericDao dao;

    @Mock
    private Note passedInNote;

    @Mock
    private Note expectedNote;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private LastUsed lastUsed;

    private Note returnedNote;
    private boolean deletedSuccess;

    @InjectMocks
    private NoteService notesService = new NoteService();

    private void andLastUsedIsUpdated() {

        verify(lastUsed).setNotebookId(NOTEBOOK_ID);
        verify(lastUsed).setPageId(PAGE_ID);
        verify(dao).merge(lastUsed);
    }

    private void andTheNoteIsPersisted() {

        verify(dao).persist(passedInNote);
    }

    private void andTheNoteIsUpdated() {

        assertEquals(expectedNote, returnedNote);
    }

    private void givenThereIsALastUsedEntry(Note note) {

        when(note.getOwner().getLastUsed()).thenReturn(lastUsed);
        when(note.getPage().getPageId()).thenReturn(PAGE_ID);
        when(note.getPage().getNotebook().getNotebookId()).thenReturn(NOTEBOOK_ID);
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

        when(dao.find(Note.class, NOTE_ID)).thenReturn(expectedNote);
        when(dao.merge(passedInNote)).thenReturn(expectedNote);
        when(dao.delete(Note.class, NOTE_ID)).thenReturn(true);
    }

    @Test
    public void testCreateNoteFunctions() {

        givenThereIsALastUsedEntry(passedInNote);
        whenICallCreateNote();
        thenTheNoteIsValidated();
        andTheNoteIsPersisted();
        andLastUsedIsUpdated();
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
