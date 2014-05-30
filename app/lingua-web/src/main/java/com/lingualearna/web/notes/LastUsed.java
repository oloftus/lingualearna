package com.lingualearna.web.notes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "last_used")
public class LastUsed implements Serializable {

    private static final long serialVersionUID = 2334064167668062489L;

    private int userId;
    private int pageId;

    @Column(name = "page")
    public int getPageId() {

        return pageId;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user")
    public int getUserId() {

        return userId;
    }

    public void setPageId(int page) {

        this.pageId = page;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }
}
