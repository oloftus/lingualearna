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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingualearna.web.security.HasOwner;
import com.lingualearna.web.validator.Unique;

@NamedQueries({
        @NamedQuery(name = Page.COUNT_PAGES_BY_NAME_QUERY, query = "SELECT count(n) FROM Page n WHERE n.name = :pageName"),
        @NamedQuery(name = Page.MAX_PAGES_QUERY, query = "SELECT max(p.position) FROM Page p WHERE p.notebook.owner = :user")
})
@Entity
@Table(name = "pages")
public class Page implements Serializable, HasOwner {

    private static final long serialVersionUID = 674115054477001746L;

    public static final String PAGE_ID_FIELD = "pageId";
    public static final String POSITION_FIELD = "position";
    public static final String COUNT_PAGES_BY_NAME_QUERY = "Page.countPagesName";
    public static final String COUNT_PAGES_BY_NAME_QUERY_PARAM = "pageName";
    public static final String MAX_PAGES_QUERY = "Page.maxPages";
    public static final String MAX_PAGES_QUERY_PARAM = "user";

    private int pageId;
    private String name;
    private int position;
    private Notebook notebook;
    private List<Note> notes;

    public Note addNote(Note note) {

        getNotes().add(note);
        note.setPage(this);

        return note;
    }

    @NotBlank
    @Length(max = 45)
    @Column(name = "name")
    @Unique(namedQuery = COUNT_PAGES_BY_NAME_QUERY, valueParamName = COUNT_PAGES_BY_NAME_QUERY_PARAM,
            message = "{org.lingualearna.web.validationMessages.duplicatePage}")
    public String getName() {

        return this.name;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "notebook")
    public Notebook getNotebook() {

        return this.notebook;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    public List<Note> getNotes() {

        return this.notes;
    }

    @JsonIgnore
    @Transient
    @Override
    public String getOwnerUsername() {

        return getNotebook().getOwner().getUsername();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "page_id")
    public int getPageId() {

        return this.pageId;
    }

    @Column(name = "position")
    public int getPosition() {

        return this.position;
    }

    @Transient
    public boolean isLastUsed() {

        return getNotebook().getOwner().getLastUsed().getPageId() == getPageId();
    }

    public Note removeNote(Note note) {

        getNotes().remove(note);
        note.setPage(null);

        return note;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNotebook(Notebook notebook) {

        this.notebook = notebook;
    }

    public void setNotes(List<Note> notes) {

        this.notes = notes;
    }

    public void setPageId(int pageId) {

        this.pageId = pageId;
    }

    public void setPosition(int position) {

        this.position = position;
    }
}
