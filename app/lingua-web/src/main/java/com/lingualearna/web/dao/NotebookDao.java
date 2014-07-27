package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;

@Component
public class NotebookDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void decrementPagePositionsInInterval(Notebook notebook, int oldPosition, Integer newPosition) {

        doIncrementDecrementQuery(Page.DECREMENT_PAGE_POSITIONS_QUERY, notebook, oldPosition, newPosition);
    }

    private void doIncrementDecrementQuery(String namedQuery, Notebook notebook, int oldPosition, int newPosition) {

        doExecuteUpdateWithParams(namedQuery,
                Pair.of(Page.NOTEBOOK_PARAM, notebook),
                Pair.of(Page.PAGE_POSITIONS_OLD_POSITION_PARAM, oldPosition),
                Pair.of(Page.PAGE_POSITIONS_NEW_POSITION_PARAM, newPosition));
    }

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return doQueryAsListWithParams(Notebook.FIND_ALL_QUERY, Notebook.class,
                Pair.of(Notebook.USER_PARAM, userId));
    }

    public long getCountOfNotebooksWithName(String name) {

        return doUntypedQueryWithParams(Notebook.COUNT_NOTEBOOKS_BY_NAME_QUERY,
                Pair.of(Notebook.NOTEBOOK_NAME_PARAM, name));
    }

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public Page getPageById(int pageId) {

        return find(Page.class, pageId);
    }

    public void incrementPagePositionsInInterval(Notebook notebook, int oldPosition, Integer newPosition) {

        doIncrementDecrementQuery(Page.INCREMENT_PAGE_POSITIONS_QUERY, notebook, oldPosition, newPosition);
    }
}
