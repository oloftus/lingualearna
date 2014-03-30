package com.lingualearna.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.json.NoteModel;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.service.NotesService;

@Controller
@RequestMapping("/api")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@RequestMapping(value = "/note", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public NoteModel createNote(@RequestBody NoteModel request) {

		return null;
	}

	@RequestMapping(value = "/note/{noteId}", produces = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public NoteModel retrieveNote(@PathVariable int noteId) {

		Note note = new Note();
		note.setNoteId(11111);
		notesService.testInsert(note);
		return null;
	}

	@RequestMapping(value = "/note/{noteId}", produces = "application/json", consumes = "application/json", method = RequestMethod.PUT)
	@ResponseBody
	public NoteModel updateNote(@PathVariable int noteId, @RequestBody NoteModel request) {

		return null;
	}

	@RequestMapping(value = "/note/{noteId}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteNote(@PathVariable int noteId) {

	}
}
