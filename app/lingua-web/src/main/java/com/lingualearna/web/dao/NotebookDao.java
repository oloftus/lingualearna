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

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return doQueryAsListWithParams(Notebook.FIND_ALL_QUERY, Notebook.class,
                Pair.of(Notebook.USER_QUERY_PARAM, userId));
    }

    public long getCountOfNotebooksWithName(String name) {

        return doUntypedQueryWithParams(Notebook.COUNT_NOTEBOOKS_NAME_QUERY,
                Pair.of(Notebook.NOTEBOOK_NAME_QUERY_PARAM, name));
    }

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public Page getPageById(int pageId) {

        return find(Page.class, pageId);
    }
}
