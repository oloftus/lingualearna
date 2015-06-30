package com.lingualearna.web.notes;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.pages.Page;
import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class NoteDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public String getSourceContext(int noteId) {

        return doQueryWithParams(Note.GET_SOURCE_CONTEXT_QUERY, String.class, Pair.of(Note.NOTE_ID_PARAM, noteId));
    }

    public void decrementNotePositionsInInterval(Page page, int oldPosition, int newPosition) {

        doIncrementDecrementInIntervalQuery(Note.DECREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY, page, oldPosition,
                newPosition);
    }

    public void doDecrementNotesAfterDeleted(Page page, int oldPosition) {

        doExecuteUpdateWithParams(Note.DECREMENT_NOTE_POSITIONS_QUERY,
                Pair.of(Note.PAGE_PARAM, page),
                Pair.of(Note.NOTE_POSITIONS_OLD_POSITION_PARAM, oldPosition));
    }

    public List<Note> getRandomNotesFromNotebookForTest(int notebookId, int numberOfNotes) {

        TypedQuery<Note> query = doQueryAsQueryWithParams(Note.RANDOM_NOTES_INCLUDED_IN_TEST_QUERY, Note.class,
                Pair.of(Note.NOTEBOOK_ID_PARAM, notebookId));
        query.setMaxResults(numberOfNotes);

        List<Note> resultList = query.getResultList();

        return resultList;
    }

    public void incrementNotePositionsInInterval(Page page, int oldPosition, int newPosition) {

        doIncrementDecrementInIntervalQuery(Note.INCREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY, page, oldPosition,
                newPosition);
    }

    private void doIncrementDecrementInIntervalQuery(String namedQuery, Page page, int oldPosition, int newPosition) {

        doExecuteUpdateWithParams(namedQuery,
                Pair.of(Note.PAGE_PARAM, page),
                Pair.of(Note.NOTE_POSITIONS_OLD_POSITION_PARAM, oldPosition),
                Pair.of(Note.NOTE_POSITIONS_NEW_POSITION_PARAM, newPosition));
    }
}
