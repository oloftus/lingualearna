package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Note;

@Component
public class NoteDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public List<Note> getNotesByPage(int pageId) {

        return doQueryAsListWithParams(Note.GET_NOTES_BY_PAGE_QUERY, Note.class,
                Pair.of(Note.GET_NOTES_BY_PAGE_PAGE_ID_PARAM, pageId));
    }
}
