package com.lingualearna.web.pages;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingualearna.web.notebooks.Notebook;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.security.ownership.HasOwner;
import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.shared.pojos.Positionable;
import com.lingualearna.web.shared.validation.annotations.ListPosition;
import com.lingualearna.web.shared.validation.annotations.UniqueWithinContext;
import com.lingualearna.web.shared.validation.groups.Repositioning;

@UniqueWithinContext(namedQuery = Page.COUNT_PAGES_BY_NAME_QUERY, uniqueParam = Page.PAGE_NAME_PARAM, uniqueProperty = "name",
        contextParam = Page.NOTEBOOK_PARAM, contextProperty = "notebook", ownIdParam = Page.OWN_ID_PARAM, ownIdProperty = "pageId",
        message = "{org.lingualearna.web.validationMessages.duplicatePage}")
@ListPosition(parentParam = Page.NOTEBOOK_PARAM, parentProperty = "notebook", namedQuery = Page.MAX_POSITION_QUERY, groups = Repositioning.class)
@NamedQueries({
        @NamedQuery(name = Page.INCREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY, query = "UPDATE Page p SET p.position = p.position + 1 "
                + "WHERE :newPosition <= p.position AND p.position < :oldPosition AND p.notebook = :notebook"),
        @NamedQuery(name = Page.DECREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY, query = "UPDATE Page p SET p.position = p.position - 1 "
                + "WHERE :oldPosition < p.position AND p.position <= :newPosition AND p.notebook = :notebook"),
        @NamedQuery(name = Page.DECREMENT_PAGE_POSITIONS_QUERY, query = "UPDATE Page p SET p.position = p.position - 1 "
                + "WHERE :oldPosition <= p.position AND p.notebook = :notebook"),
        @NamedQuery(name = Page.COUNT_PAGES_BY_NAME_QUERY, query = "SELECT count(p) FROM Page p "
                + "WHERE p.name = :pageName AND p.notebook = :notebook AND p.pageId <> :ownId"),
        @NamedQuery(name = Page.MAX_POSITION_QUERY, query = "SELECT max(p.position) FROM Page p WHERE p.notebook = :notebook")
})
@Entity
@Table(name = "pages")
public class Page implements Serializable, HasOwner, Positionable {

    private static final long serialVersionUID = 674115054477001746L;

    public static final String COUNT_PAGES_BY_NAME_QUERY = "Page.countPagesName";
    public static final String MAX_POSITION_QUERY = "Page.maxPosition";
    public static final String INCREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY = "Page.incrementNotePositionsInIntervalQuery";
    public static final String DECREMENT_PAGE_POSITIONS_IN_INTERVAL_QUERY = "Page.decrementNotePositionsInIntervalQuery";
    public static final String DECREMENT_PAGE_POSITIONS_QUERY = "Page.decrementNotePositionsQuery";
    public static final String PAGE_POSITIONS_OLD_POSITION_PARAM = "oldPosition";
    public static final String PAGE_POSITIONS_NEW_POSITION_PARAM = "newPosition";
    public static final String PAGE_NAME_PARAM = "pageName";
    public static final String USER_PARAM = "user";
    public static final String NOTEBOOK_PARAM = "notebook";
    public static final String OWN_ID_PARAM = "ownId";
    public static final String PAGE_ID_FIELD = "pageId";
    public static final String POSITION_FIELD = "position";
    public static final String NOTES_FIELD = "notes";

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

    @Override
    @Transient
    public int getId() {

        return getPageId();
    }

    @NotBlank
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

    @Transient
    public int getNotebookId() {

        return getNotebook().getNotebookId();
    }

    @JsonIgnore
    @OrderBy("position")
    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    public List<Note> getNotes() {

        return this.notes;
    }

    @JsonIgnore
    @Transient
    public AppUser getOwner() {

        return getNotebook().getOwner();
    }

    @JsonIgnore
    @Transient
    @Override
    public String getOwnerUsername() {

        return getOwner().getEmailAddress();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    public int getPageId() {

        return this.pageId;
    }

    @Override
    @Column(name = "position")
    public int getPosition() {

        return this.position;
    }

    @Transient
    public boolean isLastUsed() {

        Page lastUsed = getNotebook().getOwner().getLastUsed();

        if (lastUsed != null) {
            return lastUsed.getPageId() == getPageId();
        }

        return false;
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

    @Override
    public void setPosition(int position) {

        this.position = position;
    }
}
