package com.lingualearna.web.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.pages.Page;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;

@Controller
@RequestMapping("/api")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private ObjectMapper<NoteModel, Note> notesMapper;

    @Autowired
    private NoteModelFlattener noteModelFlattener;

    @RequestMapping(value = "/note", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public NoteModel createNote(@RequestBody @Valid NoteModel incomingNoteModel) {

        Note noteEntity = new Note();
        notesMapper.copyPropertiesLtr(incomingNoteModel, noteEntity, Note.NOTE_ID_FIELD, Note.POSITION_FIELD);
        noteService.createNote(noteEntity, incomingNoteModel.getPageId());

        return convertNoteToNoteModel(noteEntity, (String) null);
    }

    @RequestMapping(value = "/note/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteNote(@PathVariable int noteId) {

        noteService.deleteNote(noteId);
    }

    @RequestMapping(value = "/note/{noteId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public NoteModel retrieveNote(@PathVariable int noteId) {

        Note noteEntity = noteService.retrieveNote(noteId);
        return convertNoteToNoteModel(noteEntity, Note.SOURCE_CONTEXT_FIELD);
    }

    @RequestMapping(value = "/page/{pageId}/notes", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<NoteModel> retrieveNotesByPage(@PathVariable int pageId) {

        List<Note> notes = noteService.retrieveNotesByPage(pageId);

        if (notes.isEmpty()) {
            return Collections.emptyList();
        }

        List<NoteModel> noteModels = new ArrayList<>();
        for (Note note : notes) {
            NoteModel noteModel = convertNoteToNoteModel(note, Note.SOURCE_CONTEXT_FIELD);
            noteModels.add(noteModel);
        }

        return noteModels;
    }

    @RequestMapping(value = "/note/{noteId}", produces = "application/json",
            consumes = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    public NoteModel updateNote(@PathVariable int noteId, @RequestBody @Valid NoteModel incomingNote) {

        Note noteShell = new Note();
        notesMapper.copyPropertiesLtr(incomingNote, noteShell, Note.NOTE_ID_FIELD, Note.SOURCE_URL_FIELD,
                Note.SOURCE_CONTEXT_FIELD);
        noteShell.setNoteId(noteId);
        Page pageShell = new Page();
        pageShell.setPageId(incomingNote.getPageId());
        noteShell.setPage(pageShell);

        Note updatedNote = noteService.updateNote(noteShell);

        return convertNoteToNoteModel(updatedNote, Note.SOURCE_CONTEXT_FIELD);
    }

    private NoteModel convertNoteToNoteModel(Note noteEntity, String... excludes) {

        NoteModel noteModel = new NoteModel();
        notesMapper.copyPropertiesRtl(noteEntity, noteModel, excludes);
        noteModel.setPageId(noteEntity.getPage().getPageId());
        noteModelFlattener.flattenNoteModel(noteModel);

        return noteModel;
    }
}
