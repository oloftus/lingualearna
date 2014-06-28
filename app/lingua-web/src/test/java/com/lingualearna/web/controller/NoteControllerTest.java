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

import com.lingualearna.web.controller.exceptions.ResourceNotFoundException;
import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.controller.model.NoteModel;
import com.lingualearna.web.controller.modelmappers.BeanUtilsControllerModelMapper;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.service.NoteService;
import com.lingualearna.web.service.NotebookService;

@RunWith(MockitoJUnitRunner.class)
public class NoteControllerTest {

    private static final int INVALID_NOTE_ID = 0;
    private static final String NOTE_ID_FIELD_NAME = "noteId";
    private static final String SOURCE_URL_FIELD_NAME = "sourceUrl";
    private static final int VALID_NOTE_ID = 1;
    private static final int PAGE_ID = 2;

    @Mock
    private NoteModel incomingNote;

    @Captor
    private ArgumentCaptor<Note> noteArg;

    @Mock
    private Note noteEntity;

    @Captor
    private ArgumentCaptor<NoteModel> noteModelArg;

    @InjectMocks
    private NoteController notesController = new NoteController();

    @Mock
    private BeanUtilsControllerModelMapper<NoteModel, Note> notesMapper;

    @Mock
    private NoteService notesService;

    @Mock
    private NotebookService notebookService;

    @Mock
    private Page page;

    private NoteModel actualNoteModel;
    private NoteModel expectedNoteModel;

    private void givenDeleteNoteServiceFunctions() {

        when(notesService.deleteNote(VALID_NOTE_ID)).thenReturn(true);
        when(notesService.deleteNote(INVALID_NOTE_ID)).thenReturn(false);
    }

    private void givenRetrieveNoteServiceFunctions() {

        when(noteEntity.getPage()).thenReturn(page);
        when(notesService.retrieveNote(VALID_NOTE_ID)).thenReturn(noteEntity);
    }

    @Before
    public void setup() {

        when(incomingNote.getPageId()).thenReturn(PAGE_ID);
        when(page.getPageId()).thenReturn(PAGE_ID);
        when(notebookService.getPageById(PAGE_ID)).thenReturn(page);
    }

    @Test
    public void testCreateNoteFunctions() throws ValidationException {

        whenICallCreateNote();
        thenTheNoteIsCreated();
        thenTheCorrectNoteIsReturned();
    }

    @Test
    public void testDeleteNoteFunctions() {

        givenDeleteNoteServiceFunctions();
        whenICallDeleteNoteWithAValidNoteId();
        thenTheNoteIsDeleted();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteNoteThrowsExceptionOnNonExistentNote() {

        givenDeleteNoteServiceFunctions();
        whenICallDeleteNoteWithAnInvalidNoteId();
    }

    @Test
    public void testRetrieveNoteFunctions() {

        givenRetrieveNoteServiceFunctions();
        whenICallRetrieveNoteWithAValidNoteId();
        thenTheCorrectNoteIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveNoteThrowsExceptionOnNonExistentNote() {

        givenRetrieveNoteServiceFunctions();
        whenICallRetrieveNoteWithAnInvalidNoteId();
    }

    @Test
    public void testUpdateNoteFunctions() throws ValidationException {

        givenRetrieveNoteServiceFunctions();
        whenICallUpdateNoteWithAValidNoteId();
        thenTheNoteIsUpdated();
        thenTheCorrectNoteIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNoteThrowsExceptionOnNonExistentNote() throws ValidationException {

        givenRetrieveNoteServiceFunctions();
        whenICallUpdateNoteWithAnInvalidNoteId();
    }

    private void theModelIsMappedToTheEntityIgnoringId() {

        verify(notesMapper).copyPropertiesFromModel(eq(incomingNote), noteArg.capture(), eq(NOTE_ID_FIELD_NAME));
        noteEntity = noteArg.getValue();
    }

    private void theModelIsMappedToTheEntityIgnoringIdAndSourceUrl() {

        verify(notesMapper).copyPropertiesFromModel(eq(incomingNote), noteArg.capture(), eq(NOTE_ID_FIELD_NAME),
                eq(SOURCE_URL_FIELD_NAME));
        noteEntity = noteArg.getValue();
        assertEquals(page, noteEntity.getPage());
    }

    private void thenTheCorrectNoteIsReturned() {

        verify(notesMapper).copyPropertiesFromEntity(eq(noteEntity), noteModelArg.capture());
        expectedNoteModel = noteModelArg.getValue();

        assertEquals(expectedNoteModel, actualNoteModel);
        assertEquals(PAGE_ID, expectedNoteModel.getPageId());
    }

    private void thenTheNoteIsCreated() throws ValidationException {

        theModelIsMappedToTheEntityIgnoringId();
        verify(notesService).createNote(noteEntity);
    }

    private void thenTheNoteIsDeleted() {

        verify(notesService).deleteNote(VALID_NOTE_ID);
    }

    private void thenTheNoteIsUpdated() throws ValidationException {

        theModelIsMappedToTheEntityIgnoringIdAndSourceUrl();
        verify(notesService).updateNote(noteEntity);
    }

    private void whenICallCreateNote() throws ValidationException {

        actualNoteModel = notesController.createNote(incomingNote);
    }

    private void whenICallDeleteNoteWithAnInvalidNoteId() {

        notesController.deleteNote(INVALID_NOTE_ID);
    }

    private void whenICallDeleteNoteWithAValidNoteId() {

        notesController.deleteNote(VALID_NOTE_ID);
    }

    private void whenICallRetrieveNoteWithAnInvalidNoteId() {

        actualNoteModel = notesController.retrieveNote(INVALID_NOTE_ID);
    }

    private void whenICallRetrieveNoteWithAValidNoteId() {

        actualNoteModel = notesController.retrieveNote(VALID_NOTE_ID);
    }

    private void whenICallUpdateNoteWithAnInvalidNoteId() throws ValidationException {

        actualNoteModel = notesController.updateNote(INVALID_NOTE_ID, incomingNote);
    }

    private void whenICallUpdateNoteWithAValidNoteId() throws ValidationException {

        actualNoteModel = notesController.updateNote(VALID_NOTE_ID, incomingNote);
    }
}
