package com.lingualearna.web.notebooks;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class NotebookDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public List<Notebook> getAllNotebooksByUser(String username) {

        return doQueryAsListWithParams(Notebook.FIND_ALL_QUERY, Notebook.class,
                Pair.of(Notebook.EMAIL_ADDRESS_PARAM, username));
    }

    public long getCountOfNotebooksWithName(String name) {

        return doUntypedQueryWithParams(Notebook.COUNT_NOTEBOOKS_BY_NAME_QUERY,
                Pair.of(Notebook.NOTEBOOK_NAME_PARAM, name));
    }
}
