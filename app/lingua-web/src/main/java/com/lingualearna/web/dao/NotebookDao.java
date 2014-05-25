package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Notebook;

@Component
public class NotebookDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return doQueryAsListWithParams(entityManager, "Notebook.findAllByUser", Notebook.class, Pair.of("user", userId));
    }
}
