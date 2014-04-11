package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.controller.exceptions.ResourceNotFoundException;
import com.lingualearna.web.controller.model.NoteModel;
import com.lingualearna.web.controller.modelmappers.BeanUtilsControllerModelMapper;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.service.NotesService;

@RunWith(MockitoJUnitRunner.class)
public class NotesControllerTest {

    private static final int INVALID_NOTE_ID = 0;
    private static final String NOTE_ID_FIELD_NAME = "noteId";
    private static final int VALID_NOTE_ID = 1;

    private NoteModel actualNoteModel;
    private NoteModel expectedNoteModel;

    @Mock
    private NoteModel incomingNote;

    @Captor
    private ArgumentCaptor<Note> noteArg;

    @Mock
    private Note noteEntity;

    @Captor
    private ArgumentCaptor<NoteModel> noteModelArg;

    @InjectMocks
    private NotesController notesController = new NotesController();

    @Mock
    private BeanUtilsControllerModelMapper<NoteModel, Note> notesMapper;

    @Mock
    private NotesService notesService;

    private void andTheCreatedNoteIsReturned() {

        theEntityIsMappedToTheModel();
        assertEquals(expectedNoteModel, actualNoteModel);
    }

    private void givenDeleteNoteServiceFunctions() {

        when(notesService.deleteNote(VALID_NOTE_ID)).thenReturn(true);
        when(notesService.deleteNote(INVALID_NOTE_ID)).thenReturn(false);
    }

    private void givenRetrieveNoteServiceFunctions() {

        when(notesService.retrieveNote(VALID_NOTE_ID)).thenReturn(noteEntity);
    }

    @Test
    public void testCreateNoteFunctions() {

        whenICallCreateNote();
        thenTheNoteIsCreated();
        andTheCreatedNoteIsReturned();
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
        thenTheRetrievedNoteIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveNoteThrowsExceptionOnNonExistentNote() {

        givenRetrieveNoteServiceFunctions();
        whenICallRetrieveNoteWithAnInvalidNoteId();
    }

    @Test
    public void testUpdateNoteFunctions() {

        givenRetrieveNoteServiceFunctions();
        whenICallUpdateNoteWithAValidNoteId();
        thenTheNoteIsUpdated();
        andTheCreatedNoteIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNoteThrowsExceptionOnNonExistentNote() {

        givenRetrieveNoteServiceFunctions();
        whenICallUpdateNoteWithAnInvalidNoteId();
    }

    private void theEntityIsMappedToTheModel() {

        verify(notesMapper).fromEntity(eq(noteEntity), noteModelArg.capture());
        expectedNoteModel = noteModelArg.getValue();
    }

    private void theModelIsMappedToTheEntityIgnoringId() {

        verify(notesMapper).fromModel(eq(incomingNote), noteArg.capture(), eq(NOTE_ID_FIELD_NAME));
        noteEntity = noteArg.getValue();
    }

    private void thenTheRetrievedNoteIsReturned() {

        theEntityIsMappedToTheModel();
        assertEquals(expectedNoteModel, actualNoteModel);
    }

    private void thenTheNoteIsCreated() {

        theModelIsMappedToTheEntityIgnoringId();
        verify(notesService).createNote(noteEntity);
    }

    private void thenTheNoteIsDeleted() {

        verify(notesService).deleteNote(VALID_NOTE_ID);
    }

    private void thenTheNoteIsUpdated() {

        theModelIsMappedToTheEntityIgnoringId();
        verify(notesService).updateNote(noteEntity);
    }

    private void whenICallCreateNote() {

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

    private void whenICallUpdateNoteWithAnInvalidNoteId() {

        actualNoteModel = notesController.updateNote(INVALID_NOTE_ID, incomingNote);
    }

    private void whenICallUpdateNoteWithAValidNoteId() {

        actualNoteModel = notesController.updateNote(VALID_NOTE_ID, incomingNote);
    }
}
