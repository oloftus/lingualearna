package com.lingualearna.web.notes;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingualearna.web.security.HasOwner;

@Entity
@Table(name = "pages")
public class Page implements Serializable, HasOwner {

    private static final long serialVersionUID = 674115054477001746L;

    private int pageId;
    private String name;
    private int position;
    private Notebook notebook;
    private List<Note> notes;
    private LastUsed lastUsed;

    public Note addNote(Note note) {

        getNotes().add(note);
        note.setPage(this);

        return note;
    }

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "page_id")
    private LastUsed getLastUsedEntity() {

        return lastUsed;
    }

    @Length(max = 45)
    @Column(name = "name")
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
    @OneToMany(mappedBy = "page")
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

        return getLastUsedEntity() != null;
    }

    public Note removeNote(Note note) {

        getNotes().remove(note);
        note.setPage(null);

        return note;
    }

    private void setLastUsedEntity(LastUsed lastUsed) {

        this.lastUsed = lastUsed;
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
