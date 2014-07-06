package com.lingualearna.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.exceptions.ResourceNotFoundException;
import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.controller.model.NoteModel;
import com.lingualearna.web.controller.modelmappers.ControllerModelMapper;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.service.NoteService;
import com.lingualearna.web.service.NotebookService;

@Controller
@RequestMapping("/api")
public class NoteController {

    private static final String NOTE_NOT_FOUND = "Note not found";
    private static final String PAGE_NOT_FOUND = "Page not found";

    @Autowired
    private NoteService notesService;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private ControllerModelMapper<NoteModel, Note> notesMapper;

    @RequestMapping(value = "/note", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public NoteModel createNote(@RequestBody @Valid NoteModel incomingNote) throws ValidationException {

        Page page = notebookService.getPageById(incomingNote.getPageId());

        Note noteEntity = new Note();
        notesMapper.copyPropertiesFromModel(incomingNote, noteEntity, Note.NOTE_ID_FIELD, Note.POSITION_FIELD);
        noteEntity.setPage(page);
        notesService.createNote(noteEntity);

        NoteModel noteModel = new NoteModel();
        notesMapper.copyPropertiesFromEntity(noteEntity, noteModel);
        noteModel.setPageId(page.getPageId());

        return noteModel;
    }

    @RequestMapping(value = "/note/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteNote(@PathVariable int noteId) {

        boolean found = notesService.deleteNote(noteId);
        if (!found) {
            throw new ResourceNotFoundException(NOTE_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/note/{noteId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public NoteModel retrieveNote(@PathVariable int noteId) {

        Note noteEntity = notesService.retrieveNote(noteId);
        if (noteEntity == null) {
            throw new ResourceNotFoundException(NOTE_NOT_FOUND);
        }

        NoteModel noteModel = new NoteModel();
        notesMapper.copyPropertiesFromEntity(noteEntity, noteModel);
        noteModel.setPageId(noteEntity.getPage().getPageId());

        return noteModel;
    }

    @RequestMapping(value = "/page/{pageId}/notes", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<NoteModel> retrieveNotesByPage(@PathVariable int pageId) {

        List<Note> notes = notesService.retrieveNotesByPage(pageId);
        if (notes.size() == 0) {
            throw new ResourceNotFoundException(PAGE_NOT_FOUND);
        }

        List<NoteModel> noteModels = new ArrayList<>();
        for (Note note : notes) {
            NoteModel noteModel = new NoteModel();
            notesMapper.copyPropertiesFromEntity(note, noteModel);
            noteModel.setPageId(note.getPage().getPageId());
            noteModels.add(noteModel);
        }

        return noteModels;
    }

    @RequestMapping(value = "/note/{noteId}", produces = "application/json", consumes = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    public NoteModel updateNote(@PathVariable int noteId, @RequestBody NoteModel incomingNote)
            throws ValidationException {

        Note noteEntity = notesService.retrieveNote(noteId);
        if (noteEntity == null) {
            throw new ResourceNotFoundException(NOTE_NOT_FOUND);
        }

        Page page = notebookService.getPageById(incomingNote.getPageId());

        notesMapper.copyPropertiesFromModel(incomingNote, noteEntity, Note.NOTE_ID_FIELD, Note.SOURCE_URL_FIELD,
                Note.POSITION_FIELD);
        noteEntity.setPage(page);
        notesService.updateNote(noteEntity);

        NoteModel noteModel = new NoteModel();
        notesMapper.copyPropertiesFromEntity(noteEntity, noteModel);
        noteModel.setPageId(page.getPageId());

        return noteModel;
    }
}
