package com.lingualearna.web.notes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lingualearna.web.security.User;

@Entity
@Table(name = "last_used")
public class LastUsed implements Serializable {

    private static final long serialVersionUID = 2334064167668062489L;

    private User user;
    private Page page;
    private Notebook notebook;

    @ManyToOne
    @JoinColumn(name = "notebook")
    public Notebook getNotebook() {

        return notebook;
    }

    @ManyToOne
    @JoinColumn(name = "page")
    public Page getPage() {

        return page;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @OneToOne
    @JoinColumn(name = "user")
    public User getUser() {

        return user;
    }

    public void setNotebook(Notebook notebook) {

        this.notebook = notebook;
    }

    public void setPage(Page page) {

        this.page = page;
    }

    public void setUser(User user) {

        this.user = user;
    }
}
