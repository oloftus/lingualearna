package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NoteDao;
import com.lingualearna.web.languages.LanguageNamesValidator;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.OwnedObjectType;
import com.lingualearna.web.security.User;

@Service
@Transactional
public class NoteService extends AbstractService {

    @Autowired
    private NoteDao dao;

    @Autowired
    Validator validator;

    @Autowired
    private LanguageNamesValidator langNamesValidator;

    public void createNote(Note note) throws ValidationException {

        validateEntity(note);
        validateLanguageNames(note);
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

    @Override
    protected Validator getValidator() {

        return validator;
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

    @OwnedObjectType(Page.class)
    @Secured(ALLOW_OWNER)
    public List<Note> retrieveNotesByPage(int pageId) {

        return dao.getNotesByPage(pageId);
    }

    private void setLastUsed(int noteId) {

        Note note = dao.find(Note.class, noteId);
        setLastUsed(note);
    }

    private void setLastUsed(Note note) {

        User owner = note.getOwner();
        owner.setLastUsed(note.getPage());
        dao.merge(owner);
    }

    @Secured(ALLOW_OWNER)
    public Note updateNote(Note note) throws ValidationException {

        validateEntity(note);
        validateLanguageNames(note);
        Note mergedNote = dao.merge(note);
        setLastUsed(mergedNote);
        return mergedNote;
    }

    void validateLanguageNames(Note note) throws ValidationException {

        String foreignLang = note.getForeignLang().getLanguage();
        String localLang = note.getLocalLang().getLanguage();
        langNamesValidator.validateLanguageNames(foreignLang, localLang);
    }
}
