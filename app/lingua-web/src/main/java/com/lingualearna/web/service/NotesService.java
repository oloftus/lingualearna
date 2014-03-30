package com.lingualearna.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.NotesDao;
import com.lingualearna.web.notes.Note;

@Service
public class NotesService {

	@Autowired
	private NotesDao notesDao;

	public void testInsert(Note note) {

		notesDao.testInsert(note);
	}
}
