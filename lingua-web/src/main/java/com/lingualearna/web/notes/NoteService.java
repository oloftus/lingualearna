package com.lingualearna.web.notes;

import static com.lingualearna.web.security.ownership.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.pages.Page;
import com.lingualearna.web.pages.PageService;
import com.lingualearna.web.security.ownership.OwnedObjectType;
import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.shared.components.AbstractService;
import com.lingualearna.web.shared.exceptions.ResourceNotFoundException;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;
import com.lingualearna.web.shared.validation.annotations.ListPosition;
import com.lingualearna.web.shared.validation.validators.LanguageNamesValidator;

@Service
@Transactional
public class NoteService extends AbstractService {

    private static final String NOTE_NOT_FOUND = "Note not found";

    @Autowired
    private PageService pageService;

    @Autowired
    private NoteDao dao;

    @Autowired
    private Validator validator;

    @Autowired
    private LanguageNamesValidator langNamesValidator;

    @Autowired
    private ObjectMapper<Note, Note> noteMapper;

    @Override
    protected Validator getValidator() {

        return validator;
    }

    public void createNote(Note note, int pageId) {

        setPage(note, pageId);
        trimSourceContext(note);
        setNotePositionToBottom(note);
        validateEntity(note);
        validateLanguageNames(note);
        setLastUsed(note);
        dao.persist(note);
    }

    @OwnedObjectType(Note.class)
    @Secured(ALLOW_OWNER)
    public void deleteNote(int noteId) {

        Note note = dao.find(Note.class, noteId);

        if (note == null) {
            throw new ResourceNotFoundException(NoteService.NOTE_NOT_FOUND);
        }

        dao.delete(Note.class, noteId);
        dao.doDecrementNotesAfterDeleted(note.getPage(), note.getPosition());
    }

    @OwnedObjectType(Note.class)
    @Secured(ALLOW_OWNER)
    public Note retrieveNote(int noteId) {

        Note note = dao.find(Note.class, noteId);

        if (note == null) {
            throw new ResourceNotFoundException(NOTE_NOT_FOUND);
        }

        setLastUsed(note);
        return note;
    }

    @OwnedObjectType(Page.class)
    @Secured(ALLOW_OWNER)
    public List<Note> retrieveNotesByPage(int pageId) {

        Page page = dao.find(Page.class, pageId);
        return page.getNotes();
    }

    @Secured(ALLOW_OWNER)
    public Note updateNote(Note noteShell) {

        Note noteEntity = retrieveNoteByShell(noteShell);
        int oldPosition = noteEntity.getPosition();
        Page oldPage = noteEntity.getPage();

        noteMapper.copyPropertiesLtr(noteShell, noteEntity);
        setPage(noteEntity, noteShell.getPage().getPageId());

        validateEntity(noteEntity);
        validateLanguageNames(noteEntity);

        reorderNotesIfPositionChanged(noteEntity, oldPosition);
        moveNoteToBottomIfPageChanged(noteEntity, oldPage);
        setLastUsed(noteEntity);

        return dao.merge(noteEntity);
    }

    private Note retrieveNoteByShell(Note noteShell) {

        return retrieveNote(noteShell.getNoteId());
    }

    private void reorderNotesIfPositionChanged(Note noteEntity, int oldPosition) {

        validateEntity(noteEntity, ListPosition.class);

        int newPosition = noteEntity.getPosition();
        if (oldPosition != newPosition) {
            if (newPosition > oldPosition) {
                dao.decrementNotePositionsInInterval(noteEntity.getPage(), oldPosition, newPosition);
            }
            else {
                dao.incrementNotePositionsInInterval(noteEntity.getPage(), oldPosition, newPosition);
            }
        }
    }

    private void validateLanguageNames(Note note) {

        String foreignLang = note.getForeignLang().getLanguage();
        String localLang = note.getLocalLang().getLanguage();
        langNamesValidator.validateLanguageNames(foreignLang, localLang);
    }

    private void setLastUsed(Note note) {

        AppUser owner = note.getOwner();
        owner.setLastUsed(note.getPage());
        dao.merge(owner);
    }

    private void setPage(Note note, int pageId) {

        Page page = pageService.retrievePage(pageId);
        note.setPage(page);
    }

    private void trimSourceContext(Note note) {

        String sourceContext = note.getSourceContext();

        if (sourceContext != null && sourceContext.length() > Note.SOURCE_CONTEXT_MAX_LENGTH) {
            int lowerBound = Note.SOURCE_CONTEXT_MAX_LENGTH / 2;
            int upperBound = lowerBound + Note.SOURCE_CONTEXT_MAX_LENGTH;

            note.setSourceContext(sourceContext.substring(lowerBound, upperBound));
        }
    }

    private void moveNoteToBottomIfPageChanged(Note noteEntity, Page oldPage) {

        if (!noteEntity.getPage().equals(oldPage)) {
            setNotePositionToBottom(noteEntity);
        }
    }

    private void setNotePositionToBottom(Note note) {

        Integer currentMaxPosition = dao.doUntypedQueryWithParams(Note.MAX_POSITION_QUERY,
                Pair.of(Note.PAGE_PARAM, note.getPage()));

        if (currentMaxPosition == null) {
            currentMaxPosition = 0;
        }

        note.setPosition(currentMaxPosition + 1);
    }
}
