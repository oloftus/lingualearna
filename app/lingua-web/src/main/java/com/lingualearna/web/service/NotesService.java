package com.lingualearna.web.service;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.notes.Note;

@Service
@Transactional
public class NotesService {

    @Autowired
    private GenericDao<Note> notesDao;

    @Autowired
    private Validator validator;

    public void createNote(Note note) {

        validateNote(note);
        notesDao.persist(note);
    }

    public boolean deleteNote(int noteId) {

        return notesDao.delete(noteId);
    }

    @PostConstruct
    public void init() {

        notesDao.setEntityType(Note.class);
    }

    public Note retrieveNote(int noteId) {

        return notesDao.findNoLock(noteId);
    }

    public Note updateNote(Note note) {

        validateNote(note);
        return notesDao.merge(note);
    }

    private void validateNote(Note note) {

        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
