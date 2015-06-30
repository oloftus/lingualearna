package com.lingualearna.web.pages;

import java.util.Collections;
import java.util.List;

import com.lingualearna.web.notes.Note;

public class PageModel {

    private int pageId;
    private String name;
    private Integer position;
    private int notebookId;
    private List<Note> notes;

    public PageModel() {

        this.notes = Collections.emptyList();
    }

    public String getName() {

        return name;
    }

    public int getNotebookId() {

        return notebookId;
    }

    public List<Note> getNotes() {

        return notes;
    }

    public int getPageId() {

        return pageId;
    }

    public Integer getPosition() {

        return position;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNotebookId(int notebookId) {

        this.notebookId = notebookId;
    }

    public void setNotes(List<Note> notes) {

        this.notes = notes;
    }

    public void setPageId(int pageId) {

        this.pageId = pageId;
    }

    public void setPosition(Integer position) {

        this.position = position;
    }
}
