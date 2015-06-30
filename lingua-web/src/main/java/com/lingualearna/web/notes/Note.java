package com.lingualearna.web.notes;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.lingualearna.web.knowledgetest.TestEntry;
import com.lingualearna.web.pages.Page;
import com.lingualearna.web.security.ownership.HasOwner;
import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.shared.pojos.Positionable;
import com.lingualearna.web.shared.validation.annotations.DependentPropertyNotNullOrEmpty;
import com.lingualearna.web.shared.validation.annotations.ListPosition;
import com.lingualearna.web.shared.validation.annotations.MinimumOnePropertyNotEmpty;

@DependentPropertyNotNullOrEmpty.Properties({
        @DependentPropertyNotNullOrEmpty(propertyName = "foreignNote", dependentPropertyName = "foreignLang"),
        @DependentPropertyNotNullOrEmpty(propertyName = "localNote", dependentPropertyName = "localLang")
})
@MinimumOnePropertyNotEmpty(propertyNames = { "additionalNotes", "foreignNote", "localNote" })
@ListPosition(parentParam = Note.PAGE_PARAM, parentProperty = "page", namedQuery = Note.MAX_POSITION_QUERY, groups = ListPosition.class)
@NamedQueries({
        @NamedQuery(name = Note.INCREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY, query = "UPDATE Note n SET n.position = n.position + 1 "
                + "WHERE :newPosition <= n.position AND n.position < :oldPosition AND n.page = :page"),
        @NamedQuery(name = Note.DECREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY, query = "UPDATE Note n SET n.position = n.position - 1 "
                + "WHERE :oldPosition < n.position AND n.position <= :newPosition AND n.page = :page"),
        @NamedQuery(name = Note.DECREMENT_NOTE_POSITIONS_QUERY, query = "UPDATE Note n SET n.position = n.position - 1 "
                + "WHERE :oldPosition <= n.position AND n.page = :page"),
        @NamedQuery(name = Note.MAX_POSITION_QUERY, query = "SELECT max(n.position) FROM Note n WHERE n.page = :page"),
        @NamedQuery(name = Note.RANDOM_NOTES_INCLUDED_IN_TEST_QUERY, query = "SELECT n FROM Note n "
                + "WHERE n.includedInTest = true AND n.page.notebook.notebookId = :notebookId AND n.foreignNote <> '' AND n.localNote <> '' order by random()"),
        @NamedQuery(name = Note.GET_SOURCE_CONTEXT_QUERY, query = "SELECT n.sourceContext FROM Note n where n.noteId = :noteId")
})
@Entity
@Table(name = "notes")
public class Note implements Serializable, HasOwner, TestEntry, Positionable {

    private static final long serialVersionUID = -1700378910934447911L;

    public static final String INCREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY = "Note.incrementNotePositionsInIntervalQuery";
    public static final String DECREMENT_NOTE_POSITIONS_IN_INTERVAL_QUERY = "Note.decrementNotePositionsInIntervalQuery";
    public static final String DECREMENT_NOTE_POSITIONS_QUERY = "Note.decrementNotePositionsQuery";
    public static final String MAX_POSITION_QUERY = "Note.maxPosition";
    public static final String RANDOM_NOTES_INCLUDED_IN_TEST_QUERY = "Note.randomNotesIncludedInTest";
    public static final String GET_SOURCE_CONTEXT_QUERY = "Note.getSourceContext";

    public static final String NOTE_POSITIONS_OLD_POSITION_PARAM = "oldPosition";
    public static final String NOTE_POSITIONS_NEW_POSITION_PARAM = "newPosition";
    public static final String PAGE_PARAM = "page";
    public static final String NOTEBOOK_ID_PARAM = "notebookId";
    public static final String NOTE_ID_PARAM = "noteId";

    public static final String POSITION_FIELD = "position";
    public static final String NOTE_ID_FIELD = "noteId";
    public static final String SOURCE_URL_FIELD = "sourceUrl";
    public static final String SOURCE_CONTEXT_FIELD = "sourceContext";
    public static final int SOURCE_CONTEXT_MAX_LENGTH = 500;

    private Integer noteId;
    private String additionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private Locale localLang;
    private String localNote;
    private String sourceUrl;
    private int position;
    private TranslationSource translationSource;
    private Page page;
    private boolean includedInTest;
    private boolean starred;
    private String sourceContext;

    @Override
    @Transient
    public int getId() {

        return getNoteId();
    }

    @Override
    @Transient
    public boolean getHasSourceContext() {

        return !(sourceContext == null || sourceContext.trim().isEmpty());
    }

    @Length(max = SOURCE_CONTEXT_MAX_LENGTH)
    @Column(name = "source_context")
    public String getSourceContext() {

        return sourceContext;
    }

    public void setSourceContext(String sourceContext) {

        this.sourceContext = sourceContext;
    }

    @Length(max = 10000)
    @Column(name = "additional_notes")
    public String getAdditionalNotes() {

        return additionalNotes;
    }

    @Column(name = "foreign_lang")
    public Locale getForeignLang() {

        return foreignLang;
    }

    @Length(max = 2000)
    @Column(name = "foreign_note")
    @Override
    public String getForeignNote() {

        return foreignNote;
    }

    @Column(name = "local_lang")
    public Locale getLocalLang() {

        return localLang;
    }

    @Length(max = 2000)
    @Column(name = "local_note")
    @Override
    public String getLocalNote() {

        return localNote;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    @Override
    public Integer getNoteId() {

        return noteId;
    }

    @Transient
    public AppUser getOwner() {

        return getPage().getNotebook().getOwner();
    }

    @Transient
    @Override
    public String getOwnerUsername() {

        return getOwner().getEmailAddress();
    }

    @ManyToOne
    @JoinColumn(name = "page")
    public Page getPage() {

        return page;
    }

    @Override
    @Column(name = "position")
    public int getPosition() {

        return position;
    }

    @URL
    @Length(max = 2000)
    @Column(name = "source_url")
    public String getSourceUrl() {

        return sourceUrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "translation_source")
    public TranslationSource getTranslationSource() {

        return translationSource;
    }

    @Column(name = "include_test")
    public boolean isIncludedInTest() {

        return includedInTest;
    }

    @Column(name = "starred")
    public boolean isStarred() {

        return starred;
    }

    public void setAdditionalNotes(String additionalNotes) {

        this.additionalNotes = additionalNotes;
    }

    public void setForeignLang(Locale foreignLang) {

        this.foreignLang = foreignLang;
    }

    public void setForeignNote(String foreignNote) {

        this.foreignNote = foreignNote;
    }

    public void setIncludedInTest(boolean includedInTest) {

        this.includedInTest = includedInTest;
    }

    public void setLocalLang(Locale localLang) {

        this.localLang = localLang;
    }

    public void setLocalNote(String localNote) {

        this.localNote = localNote;
    }

    public void setNoteId(Integer noteId) {

        this.noteId = noteId;
    }

    public void setPage(Page page) {

        this.page = page;
    }

    @Override
    public void setPosition(int position) {

        this.position = position;
    }

    public void setSourceUrl(String sourceUrl) {

        this.sourceUrl = sourceUrl;
    }

    public void setStarred(boolean starred) {

        this.starred = starred;
    }

    public void setTranslationSource(TranslationSource translationSource) {

        this.translationSource = translationSource;
    }
}
