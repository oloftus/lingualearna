package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
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
    private static final int VALID_NOTE_ID = 1;
    private static final int PAGE_ID = 2;
    private static final Integer OLD_POSITION = 1;
    private static final Integer NEW_POSITION = 2;

    @Captor
    private ArgumentCaptor<Note> noteArg;

    @Captor
    private ArgumentCaptor<NoteModel> noteModelArg;

    @Mock
    private NoteModel incomingNote;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Note noteEntity;

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
    private List<NoteModel> actualNoteModels;

    @InjectMocks
    private NoteController notesController = new NoteController();

    private void andTheCorrectNoteIsReturned() {

        verify(notesMapper).copyPropertiesFromEntity(eq(noteEntity), noteModelArg.capture());
        expectedNoteModel = noteModelArg.getValue();

        assertEquals(expectedNoteModel, actualNoteModel);
        assertEquals(PAGE_ID, expectedNoteModel.getPageId());
    }

    private void andTheNoteIsCreated() throws ValidationException {

        verify(notesService).createNote(noteEntity);
    }

    private void andTheNoteIsUpdated() throws ValidationException {

        verify(notesService).updateNote(noteEntity);
    }

    private void andTheNoteIsUpdatedWithPosition() throws ValidationException {

        verify(notesService).updateNoteWithPosition(noteEntity, OLD_POSITION);
    }

    private void andTheNoteModelsAreReturned() {

        assertEquals(1, actualNoteModels.size());
        assertEquals(actualNoteModels.get(0), actualNoteModel);
    }

    private void andTheNotePositionHasChanged() {

        Answer<Void> answer = new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                when(noteEntity.getPosition()).thenReturn(NEW_POSITION);
                return null;
            }

        };

        doAnswer(answer).when(notesMapper).copyPropertiesFromModel(incomingNote, noteEntity, Note.NOTE_ID_FIELD,
                Note.SOURCE_URL_FIELD);
    }

    private void givenDeleteNoteServiceFunctions() {

        when(notesService.deleteNote(VALID_NOTE_ID)).thenReturn(true);
        when(notesService.deleteNote(INVALID_NOTE_ID)).thenReturn(false);
    }

    private void givenNotesExist() {

        when(noteEntity.getPage().getPageId()).thenReturn(PAGE_ID);
        List<Note> notes = Lists.newArrayList(noteEntity);
        when(notesService.retrieveNotesByPage(PAGE_ID)).thenReturn(notes);
    }

    private void givenRetrieveNoteServiceFunctions() {

        when(noteEntity.getPage()).thenReturn(page);
        when(noteEntity.getPosition()).thenReturn(OLD_POSITION);
        when(notesService.retrieveNote(VALID_NOTE_ID)).thenReturn(noteEntity);
    }

    private void givenThePageDoesNotExist() {

        when(notesService.retrieveNotesByPage(PAGE_ID)).thenReturn(null);
    }

    private void givenThePageHasNoNotes() {

        List<Note> notes = Collections.emptyList();
        when(notesService.retrieveNotesByPage(PAGE_ID)).thenReturn(notes);
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
        thenTheModelIsMappedToTheEntityIgnoringIdAndPosition();
        andTheNoteIsCreated();
        andTheCorrectNoteIsReturned();
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
        andTheCorrectNoteIsReturned();
    }

    @Test
    public void testRetrieveNotesByPageCopiesMapsNotesCorrectly() {

        givenNotesExist();
        whenICallRetrieveNotesByPage();
        thenThePropertiesAreCopiedAcross();
        andTheNoteModelsAreReturned();
    }

    @Test
    public void testRetrieveNotesByPageReturnsEmptyListForSizeZero() {

        givenThePageHasNoNotes();
        whenICallRetrieveNotesByPage();
        thenAnEmptyListIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveNotesByPageThrowsExceptionOnNonExistentPage() {

        givenThePageDoesNotExist();
        whenICallRetrieveNotesByPage();
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
        thenTheModelIsMappedToTheEntityIgnoringIdSourceUrl();
        andTheNoteIsUpdated();
        andTheCorrectNoteIsReturned();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNoteThrowsExceptionOnNonExistentNote() throws ValidationException {

        givenRetrieveNoteServiceFunctions();
        whenICallUpdateNoteWithAnInvalidNoteId();
    }

    @Test
    public void testUpdateUpdatesWithPositionsWhenPositionsHaveChanged() throws ValidationException {

        givenRetrieveNoteServiceFunctions();
        andTheNotePositionHasChanged();
        whenICallUpdateNoteWithAValidNoteId();
        thenTheModelIsMappedToTheEntityIgnoringIdSourceUrl();
        andTheNoteIsUpdatedWithPosition();
        andTheCorrectNoteIsReturned();
    }

    private void thenAnEmptyListIsReturned() {

        assertEquals(Collections.emptyList(), actualNoteModels);
    }

    private void thenTheModelIsMappedToTheEntityIgnoringIdAndPosition() {

        verify(notesMapper).copyPropertiesFromModel(eq(incomingNote), noteArg.capture(), eq(Note.NOTE_ID_FIELD),
                eq(Note.POSITION_FIELD));
        noteEntity = noteArg.getValue();
        assertEquals(page, noteEntity.getPage());
    }

    private void thenTheModelIsMappedToTheEntityIgnoringIdSourceUrl() {

        verify(notesMapper).copyPropertiesFromModel(eq(incomingNote), noteArg.capture(), eq(Note.NOTE_ID_FIELD),
                eq(Note.SOURCE_URL_FIELD));
        noteEntity = noteArg.getValue();
        assertEquals(page, noteEntity.getPage());
    }

    private void thenTheNoteIsDeleted() {

        verify(notesService).deleteNote(VALID_NOTE_ID);
    }

    private void thenThePropertiesAreCopiedAcross() {

        verify(notesMapper).copyPropertiesFromEntity(eq(noteEntity), noteModelArg.capture());
        actualNoteModel = noteModelArg.getValue();
        assertEquals(PAGE_ID, actualNoteModel.getPageId());
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

    private void whenICallRetrieveNotesByPage() {

        actualNoteModels = notesController.retrieveNotesByPage(PAGE_ID);
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
