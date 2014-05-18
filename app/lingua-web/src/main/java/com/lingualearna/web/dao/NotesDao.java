package com.lingualearna.web.dao;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.User;

@Component
public class NotesDao extends GenericDao<Note> {

    private static final String GET_OWNER_QUERY = "Note.getOwner";
    private static final String NOTE_ID_PARAM = "noteId";

    public User getNoteOwner(int noteId) {

        TypedQuery<User> query = getEntityManager().createNamedQuery(GET_OWNER_QUERY, User.class);
        query.setParameter(NOTE_ID_PARAM, noteId);
        return query.getSingleResult();
    }

    @PostConstruct
    public void init() {

        setEntityType(Note.class);
    }
}
