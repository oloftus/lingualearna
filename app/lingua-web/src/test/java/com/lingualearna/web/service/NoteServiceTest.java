package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Locale;
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

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.languages.LanguageNamesValidator;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.User;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {

    private static final Locale FOREIGN_LANG = Locale.forLanguageTag("en");
    private static final Locale LOCAL_LANG = Locale.forLanguageTag("fr");
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

    @Mock
    private LanguageNamesValidator langNamesValidator;

    private Note returnedNote;
    private boolean deletedSuccess;

    @InjectMocks
    private NoteService notesService = new NoteService();

    private void andLastUsedIsUpdated() {

        verify(owner).setLastUsed(page);
        verify(dao).merge(owner);
    }

    private void andTheLastUsedIsNotUpdated() {

        verify(owner, never()).setLastUsed(page);
    }

    @Before
    public void setup() {

        setupNotes();
        setupNotesDao();
        setupLastUsedEntryFor(passedInNote);
        setupLastUsedEntryFor(expectedNote);
    }

    private void setupInvalidNote() {

        Set<ConstraintViolation<Note>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(passedInNote)).thenReturn(violations);
    }

    private void setupLastUsedEntryFor(Note note) {

        when(page.getPageId()).thenReturn(PAGE_ID);
        when(page.getNotebook()).thenReturn(notebook);
        when(notebook.getNotebookId()).thenReturn(NOTEBOOK_ID);
        when(note.getOwner()).thenReturn(owner);
        when(owner.getLastUsed()).thenReturn(page);
        when(note.getPage()).thenReturn(page);
    }

    private void setupNotes() {

        when(passedInNote.getNoteId()).thenReturn(NOTE_ID);
        when(passedInNote.getForeignLang()).thenReturn(FOREIGN_LANG);
        when(passedInNote.getLocalLang()).thenReturn(LOCAL_LANG);

        when(expectedNote.getNoteId()).thenReturn(NOTE_ID);
    }

    private void setupNotesDao() {

        when(dao.find(Note.class, NOTE_ID)).thenReturn(expectedNote);
        when(dao.merge(passedInNote)).thenReturn(expectedNote);
        when(dao.delete(Note.class, NOTE_ID)).thenReturn(true);
    }

    private void setupNoteWithInvalidLanguages() throws ValidationException {

        doThrow(new ValidationException()).when(langNamesValidator).validateLanguageNames((String) anyVararg());
    }

    @Test
    public void testCreateNoteAcceptsValidNote() throws ValidationException {

        whenICallCreateNote();
        thenTheNoteIsPersisted();
        andLastUsedIsUpdated();
    }

    @Test(expected = ValidationException.class)
    public void testCreateNoteRejectsInvalidLanguages() throws ValidationException {

        whenICallCreateNoteWithANoteWithInvalidLanguagesThenAnExceptionIsThrown();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateNoteRejectsInValidNote() throws ValidationException {

        whenICallCreateNoteWithAnInvalidNoteThenAnExceptionIsThrown();
    }

    @Test
    public void testDeleteNoteFunctions() {

        whenICallDeleteNote();
        thenTheNoteIsDeleted();
        andLastUsedIsUpdated();
    }

    @Test
    public void testDeleteNoteFunctionsForNonExistentNotes() throws ValidationException {

        whenICallDeleteNoteWithANonExistentNote();
        thenTheDeletedResponseIsFalse();
        andTheLastUsedIsNotUpdated();
    }

    @Test
    public void testRetrieveNoteFunctions() {

        whenICallRetrieveNote();
        thenTheNoteIsRetrieved();
        andLastUsedIsUpdated();
    }

    @Test
    public void testRetrieveNoteFunctionsForNonExistentNotes() throws ValidationException {

        whenICallRetrieveNoteWithANonExistentNote();
        thenNothingIsReturned();
        andTheLastUsedIsNotUpdated();
    }

    @Test
    public void testUpdateNoteAcceptsValidNote() throws ValidationException {

        whenICallUpdateNote();
        thenTheNoteIsUpdated();
        andLastUsedIsUpdated();
    }

    @Test(expected = ValidationException.class)
    public void testUpdateNoteRejectsInvalidLanguages() throws ValidationException {

        whenICallUpdateNoteWithANoteWithInvalidLanguagesThenAnExceptionIsThrown();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateNoteRejectsInValidNote() throws ValidationException {

        whenICallUpdateNoteWithAnInvalidNoteThenAnExceptionIsThrown();
    }

    private void thenNothingIsReturned() {

        assertNull(returnedNote);
    }

    private void thenTheDeletedResponseIsFalse() {

        assertFalse(deletedSuccess);
    }

    private void thenTheNoteIsDeleted() {

        assertTrue(deletedSuccess);
    }

    private void thenTheNoteIsPersisted() {

        verify(dao).persist(passedInNote);
    }

    private void thenTheNoteIsRetrieved() {

        assertEquals(expectedNote, returnedNote);
    }

    private void thenTheNoteIsUpdated() {

        assertEquals(expectedNote, returnedNote);
    }

    private void whenICallCreateNote() throws ValidationException {

        notesService.createNote(passedInNote);
    }

    private void whenICallCreateNoteWithAnInvalidNoteThenAnExceptionIsThrown() throws ValidationException {

        setupInvalidNote();
        whenICallCreateNote();
    }

    private void whenICallCreateNoteWithANoteWithInvalidLanguagesThenAnExceptionIsThrown() throws ValidationException {

        setupNoteWithInvalidLanguages();
        whenICallCreateNote();
    }

    private void whenICallDeleteNote() {

        deletedSuccess = notesService.deleteNote(NOTE_ID);
    }

    private void whenICallDeleteNoteWithANonExistentNote() throws ValidationException {

        when(dao.delete(Note.class, NOTE_ID)).thenReturn(false);
        whenICallDeleteNote();
    }

    private void whenICallRetrieveNote() {

        returnedNote = notesService.retrieveNote(NOTE_ID);
    }

    private void whenICallRetrieveNoteWithANonExistentNote() throws ValidationException {

        when(dao.find(Note.class, NOTE_ID)).thenReturn(null);
        whenICallRetrieveNote();
    }

    private void whenICallUpdateNote() throws ValidationException {

        returnedNote = notesService.updateNote(passedInNote);
    }

    private void whenICallUpdateNoteWithAnInvalidNoteThenAnExceptionIsThrown() throws ValidationException {

        setupInvalidNote();
        whenICallUpdateNote();
    }

    private void whenICallUpdateNoteWithANoteWithInvalidLanguagesThenAnExceptionIsThrown() throws ValidationException {

        setupNoteWithInvalidLanguages();
        whenICallUpdateNote();
    }
}
