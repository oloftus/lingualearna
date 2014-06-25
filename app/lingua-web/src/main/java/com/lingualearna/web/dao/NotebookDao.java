package com.lingualearna.web.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;

@Component
public class NotebookDao extends AbstractDao {

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return doQueryAsListWithParams(Notebook.FIND_ALL_QUERY, Notebook.class,
                Pair.of(Notebook.USER_QUERY_PARAM, userId));
    }

    public long getCountOfNotebooksWithName(String name) {

        return doUntypedQueryWithParams(Notebook.COUNT_NOTEBOOKS_NAME_QUERY,
                Pair.of(Notebook.NOTEBOOK_NAME_QUERY_PARAM, name));
    }

    public Page getPageById(int pageId) {

        return find(Page.class, pageId);
    }
}
