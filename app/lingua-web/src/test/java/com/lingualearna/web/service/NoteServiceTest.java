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
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.User;

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
    private User owner;

    @Mock
    private Page page;

    @Mock
    private GenericDao dao;

    @Mock
    private Notebook notebook;

    @Mock
    private Note passedInNote;

    @Mock
    private Note expectedNote;

    private Note returnedNote;
    private boolean deletedSuccess;

    @InjectMocks
    private NoteService notesService = new NoteService();

    private void andLastUsedIsUpdated() {

        verify(owner).setLastUsed(page);
        verify(dao).merge(owner);
    }

    private void andTheNoteIsPersisted() {

        verify(dao).persist(passedInNote);
    }

    private void andTheNoteIsUpdated() {

        assertEquals(expectedNote, returnedNote);
    }

    private void givenLastUsedIsSetup() {

        givenThereIsALastUsedEntryFor(passedInNote);
        givenThereIsALastUsedEntryFor(expectedNote);
    }

    private void givenThereIsALastUsedEntryFor(Note note) {

        when(page.getPageId()).thenReturn(PAGE_ID);
        when(page.getNotebook()).thenReturn(notebook);
        when(notebook.getNotebookId()).thenReturn(NOTEBOOK_ID);
        when(note.getOwner()).thenReturn(owner);
        when(owner.getLastUsed()).thenReturn(page);
        when(note.getPage()).thenReturn(page);
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

        givenLastUsedIsSetup();
        whenICallCreateNote();
        thenTheNoteIsValidated();
        andTheNoteIsPersisted();
        andLastUsedIsUpdated();
    }

    @Test
    public void testDeleteNoteFunctions() {

        givenLastUsedIsSetup();
        whenICallDeleteNote();
        thenTheNoteIsDeleted();
        andLastUsedIsUpdated();
    }

    @Test
    public void testRetrieveNoteFunctions() {

        givenLastUsedIsSetup();
        whenICallRetrieveNote();
        thenTheNoteIsRetrieved();
        andLastUsedIsUpdated();
    }

    @Test
    public void testUpdateNoteFunctions() {

        givenLastUsedIsSetup();
        whenICallUpdateNote();
        thenTheNoteIsValidated();
        andTheNoteIsUpdated();
        andLastUsedIsUpdated();
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
