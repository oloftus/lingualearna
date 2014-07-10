package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.Page;

@Component
public class NoteDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void decrementPagePositionsInInterval(Page page, int oldPosition, int newPosition) {

        doIncrementDecrementQuery(Note.DECREMENT_NOTE_POSITIONS_QUERY, page, oldPosition, newPosition);
    }

    private void doIncrementDecrementQuery(String namedQuery, Page page, int oldPosition, int newPosition) {

        doExecuteUpdateWithParams(namedQuery,
                Pair.of(Note.NOTE_POSITIONS_PAGE_PARAM, page),
                Pair.of(Note.NOTE_POSITIONS_OLD_POSITION_PARAM, oldPosition),
                Pair.of(Note.NOTE_POSITIONS_NEW_POSITION_PARAM, newPosition));
    }

    @Override
    public EntityManager getEntityManager() {

        return entityManager;
    }

    public void incrementPagePositionsInInterval(Page page, int oldPosition, int newPosition) {

        doIncrementDecrementQuery(Note.INCREMENT_NOTE_POSITIONS_QUERY, page, oldPosition, newPosition);
    }
}
