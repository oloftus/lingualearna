package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.NotesDao;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.SecuredType;

@Service
@Transactional
public class NotesService {

    @Autowired
    private NotesDao notesDao;

    @Autowired
    private Validator validator;

    public void createNote(Note note) {

        validateNote(note);
        notesDao.persist(note);
        setNotesDao(note);
    }

    @SecuredType(Note.class)
    @Secured(ALLOW_OWNER)
    public boolean deleteNote(int noteId) {

        return notesDao.delete(noteId);
    }

    @SecuredType(Note.class)
    @Secured(ALLOW_OWNER)
    public Note retrieveNote(int noteId) {

        Note note = notesDao.findNoLock(noteId);
        setNotesDao(note);
        return note;
    }

    private void setNotesDao(Note note) {

        note.setNotesDao(notesDao);
    }

    @Secured(ALLOW_OWNER)
    public Note updateNote(Note note) {

        validateNote(note);
        Note mergedNote = notesDao.merge(note);
        setNotesDao(mergedNote);
        return mergedNote;
    }

    private void validateNote(Note note) {

        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
