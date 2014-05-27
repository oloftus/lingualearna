package com.lingualearna.web.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Notebook;

@Component
public class NotebookDao extends AbstractDao {

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return doQueryAsListWithParams("Notebook.findAllByUser", Notebook.class, Pair.of("user", userId));
    }
}
