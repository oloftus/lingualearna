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

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.notes.LastUsed;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.OwnedObjectType;

@Service
@Transactional
public class NoteService {

    @Autowired
    private GenericDao dao;

    @Autowired
    private Validator validator;

    public void createNote(Note note) {

        validateNote(note);
        dao.persist(note);
        setLastUsed(note);
    }

    @OwnedObjectType(Note.class)
    @Secured(ALLOW_OWNER)
    public boolean deleteNote(int noteId) {

        boolean found = dao.delete(Note.class, noteId);

        if (found) {
            setLastUsed(noteId);
        }

        return found;
    }

    @OwnedObjectType(Note.class)
    @Secured(ALLOW_OWNER)
    public Note retrieveNote(int noteId) {

        Note note = dao.find(Note.class, noteId);

        if (note != null) {
            setLastUsed(note);
        }

        return note;
    }

    private void setLastUsed(int noteId) {

        Note note = dao.find(Note.class, noteId);
        setLastUsed(note);
    }

    private void setLastUsed(Note note) {

        LastUsed lastUsed = note.getOwner().getLastUsed();
        lastUsed.setPageId(note.getPage().getPageId());
        lastUsed.setNotebookId(note.getPage().getNotebook().getNotebookId());
        dao.merge(lastUsed);
    }

    @Secured(ALLOW_OWNER)
    public Note updateNote(Note note) {

        validateNote(note);
        Note mergedNote = dao.merge(note);
        setLastUsed(mergedNote);
        return mergedNote;
    }

    private void validateNote(Note note) {

        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
