package com.lingualearna.web.controller.model;

import java.io.Serializable;

public class LastUsedModel implements Serializable {

    private static final long serialVersionUID = -656329550775045263L;

    private int notebookId;
    private int pageId;

    public int getNotebookId() {

        return notebookId;
    }

    public int getPageId() {

        return pageId;
    }

    public void setNotebookId(int notebook) {

        this.notebookId = notebook;
    }

    public void setPageId(int page) {

        this.pageId = page;
    }
}
