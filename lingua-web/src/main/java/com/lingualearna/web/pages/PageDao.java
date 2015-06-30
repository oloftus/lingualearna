package com.lingualearna.web.pages;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notebooks.Notebook;
import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class PageDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public Page getPageById(int pageId) {

        return find(Page.class, pageId);
    }

    public void incrementPagePositionsInInterval(Notebook notebook, int oldPosition, Integer newPosition) {

        doIncrementDecrementQuery(Page.INCREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY, notebook, oldPosition, newPosition);
    }

    public void decrementPagePositionsInInterval(Notebook notebook, int oldPosition, Integer newPosition) {

        doIncrementDecrementQuery(Page.DECREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY, notebook, oldPosition, newPosition);
    }

    public void doDecrementPagesAfterDeleted(Notebook notebook, int oldPosition) {

        doExecuteUpdateWithParams(Page.DECREMENT_PAGE_POSITIONS_QUERY,
                Pair.of(Page.NOTEBOOK_PARAM, notebook),
                Pair.of(Page.PAGE_POSITIONS_OLD_POSITION_PARAM, oldPosition));
    }

    private void doIncrementDecrementQuery(String namedQuery, Notebook notebook, int oldPosition, int newPosition) {

        doExecuteUpdateWithParams(namedQuery,
                Pair.of(Page.NOTEBOOK_PARAM, notebook),
                Pair.of(Page.PAGE_POSITIONS_OLD_POSITION_PARAM, oldPosition),
                Pair.of(Page.PAGE_POSITIONS_NEW_POSITION_PARAM, newPosition));
    }
}
