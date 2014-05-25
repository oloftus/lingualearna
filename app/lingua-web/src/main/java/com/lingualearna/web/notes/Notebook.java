package com.lingualearna.web.notes;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingualearna.web.security.User;

@Entity
@Table(name = "notebooks")
@NamedQuery(name = "Notebook.findAllByUser", query = "SELECT n FROM Notebook n WHERE n.owner.userId = :user")
public class Notebook implements Serializable {

    private static final long serialVersionUID = 5569311441731774188L;

    private int notebookId;
    private String foreignLang;
    private String localLang;
    private String name;
    private User owner;
    private List<Page> pages;

    public Page addPage(Page page) {

        getPages().add(page);
        page.setNotebook(this);

        return page;
    }

    @Column(name = "foreign_lang")
    public String getForeignLang() {

        return this.foreignLang;
    }

    @Column(name = "local_lang")
    public String getLocalLang() {

        return this.localLang;
    }

    @Length(max = 45)
    public String getName() {

        return this.name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notebook_id")
    public int getNotebookId() {

        return this.notebookId;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    public User getOwner() {

        return this.owner;
    }

    @OneToMany(mappedBy = "notebook", fetch = FetchType.EAGER)
    public List<Page> getPages() {

        return this.pages;
    }

    @Transient
    public boolean isLastUsed() {

        return getOwner().getLastUsed().getNotebookId() == getNotebookId();
    }

    public Page removePage(Page page) {

        getPages().remove(page);
        page.setNotebook(null);

        return page;
    }

    public void setForeignLang(String foreignLang) {

        this.foreignLang = foreignLang;
    }

    public void setLocalLang(String localLang) {

        this.localLang = localLang;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNotebookId(int notebookId) {

        this.notebookId = notebookId;
    }

    public void setOwner(User owner) {

        this.owner = owner;
    }

    public void setPages(List<Page> pages) {

        this.pages = pages;
    }
}
