package com.lingualearna.web.controller.model;

public class PageModel {

    private int pageId;
    private String name;
    private Integer position;
    private int notebookId;

    public String getName() {

        return name;
    }

    public int getNotebookId() {

        return notebookId;
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

    public void setPageId(int pageId) {

        this.pageId = pageId;
    }

    public void setPosition(Integer position) {

        this.position = position;
    }
}
