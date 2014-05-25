package com.lingualearna.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.exceptions.ResourceNotFoundException;
import com.lingualearna.web.controller.model.NoteModel;
import com.lingualearna.web.controller.modelmappers.ControllerModelMapper;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.service.NoteService;

@Controller
@RequestMapping("/api/note")
public class NoteController {

    private static final String NOTE_ID_FIELD_NAME = "noteId";
    private static final String SOURCE_URL_FIELD_NAME = "sourceUrl";

    @Autowired
    private NoteService notesService;

    @Autowired
    private ControllerModelMapper<NoteModel, Note> notesMapper;

    @RequestMapping(value = "", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public NoteModel createNote(@RequestBody NoteModel incomingNote) {

        Note noteEntity = new Note();
        notesMapper.fromModel(incomingNote, noteEntity, NOTE_ID_FIELD_NAME);
        notesService.createNote(noteEntity);

        NoteModel noteModel = new NoteModel();
        notesMapper.fromEntity(noteEntity, noteModel);

        return noteModel;
    }

    @RequestMapping(value = "/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteNote(@PathVariable int noteId) {

        boolean found = notesService.deleteNote(noteId);
        if (!found) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/{noteId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public NoteModel retrieveNote(@PathVariable int noteId) {

        Note noteEntity = notesService.retrieveNote(noteId);
        if (noteEntity == null) {
            throw new ResourceNotFoundException();
        }

        NoteModel noteModel = new NoteModel();
        notesMapper.fromEntity(noteEntity, noteModel);

        return noteModel;
    }

    @RequestMapping(value = "/{noteId}", produces = "application/json", consumes = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    public NoteModel updateNote(@PathVariable int noteId, @RequestBody NoteModel incomingNote) {

        Note noteEntity = notesService.retrieveNote(noteId);
        if (noteEntity == null) {
            throw new ResourceNotFoundException();
        }

        notesMapper.fromModel(incomingNote, noteEntity, NOTE_ID_FIELD_NAME, SOURCE_URL_FIELD_NAME);
        notesService.updateNote(noteEntity);

        NoteModel noteModel = new NoteModel();
        notesMapper.fromEntity(noteEntity, noteModel);

        return noteModel;
    }
}
