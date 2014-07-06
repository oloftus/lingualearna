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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.lingualearna.web.security.HasOwner;
import com.lingualearna.web.security.User;
import com.lingualearna.web.validator.DependentPropertyNotNullOrEmpty;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmpty;

@DependentPropertyNotNullOrEmpty.Properties({
        @DependentPropertyNotNullOrEmpty(propertyName = "foreignNote", dependentPropertyName = "foreignLang"),
        @DependentPropertyNotNullOrEmpty(propertyName = "localNote", dependentPropertyName = "localLang")
})
@MinimumOnePropertyNotEmpty(propertyNames = { "additionalNotes", "foreignNote", "localNote" })
@NamedQuery(name = Note.GET_NOTES_BY_PAGE_QUERY, query = "SELECT n FROM Note n WHERE n.page.pageId = :pageId ORDER BY n.position")
@Entity
@Table(name = "notes")
public class Note implements Serializable, HasOwner {

    private static final long serialVersionUID = -1700378910934447911L;

    public static final String GET_NOTES_BY_PAGE_QUERY = "Note.getNotesByPage";
    public static final String GET_NOTES_BY_PAGE_PAGE_ID_PARAM = "pageId";

    public static final String POSITION_FIELD = "position";
    public static final String NOTE_ID_FIELD = "noteId";
    public static final String SOURCE_URL_FIELD = "sourceUrl";

    private Integer noteId;
    private String additionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private Locale localLang;
    private String localNote;
    private String sourceUrl;
    private Integer position;
    private TranslationSource translationSource;
    private Page page;
    private boolean includedInTest;
    private boolean starred;

    @Length(max = 2000)
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
    public String getForeignNote() {

        return foreignNote;
    }

    @Column(name = "local_lang")
    public Locale getLocalLang() {

        return localLang;
    }

    @Length(max = 2000)
    @Column(name = "local_note")
    public String getLocalNote() {

        return localNote;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    public Integer getNoteId() {

        return noteId;
    }

    @Transient
    public User getOwner() {

        return getPage().getNotebook().getOwner();
    }

    @Transient
    @Override
    public String getOwnerUsername() {

        return getOwner().getUsername();
    }

    @ManyToOne
    @JoinColumn(name = "page")
    public Page getPage() {

        return page;
    }

    @Column(name = "position")
    public Integer getPosition() {

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

    public void setPosition(Integer position) {

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
