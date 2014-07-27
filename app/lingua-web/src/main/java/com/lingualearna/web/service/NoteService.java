package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.lang3.tuple.Pair;
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
        setNotePosition(note);
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

        Page page = dao.find(Page.class, pageId);

        if (page != null) {
            return page.getNotes();
        }

        return null;
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

    private void setNotePosition(Note note) {

        Integer currentMaxPosition = dao.doUntypedQueryWithParams(Note.MAX_POSITION_QUERY,
                Pair.of(Note.PAGE_PARAM, note.getPage()));
        if (currentMaxPosition == null) {
            currentMaxPosition = 0;
        }
        note.setPosition(currentMaxPosition + 1);
    }

    @Secured(ALLOW_OWNER)
    public Note updateNote(Note note) throws ValidationException {

        validateEntity(note);
        validateLanguageNames(note);
        Note mergedNote = dao.merge(note);
        setLastUsed(mergedNote);
        return mergedNote;
    }

    @Secured(ALLOW_OWNER)
    public Note updateNoteWithPosition(Note note, int oldPosition) throws ValidationException {

        Integer newPosition = note.getPosition();
        if (oldPosition < newPosition) {
            dao.decrementNotePositionsInInterval(note.getPage(), oldPosition, newPosition);
        }
        else {
            dao.incrementNotePositionsInInterval(note.getPage(), oldPosition, newPosition);
        }

        Note updatedNote = updateNote(note);
        return updatedNote;
    }

    void validateLanguageNames(Note note) throws ValidationException {

        String foreignLang = note.getForeignLang().getLanguage();
        String localLang = note.getLocalLang().getLanguage();
        langNamesValidator.validateLanguageNames(foreignLang, localLang);
    }
}
