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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.lingualearna.web.notes.validator.DependentPropertyNotNullOrEmpty;
import com.lingualearna.web.notes.validator.MinimumOnePropertyNotEmpty;
import com.lingualearna.web.security.HasOwner;

@DependentPropertyNotNullOrEmpty.Properties({
        @DependentPropertyNotNullOrEmpty(propertyName = "foreignNote", dependentPropertyName = "foreignLang"),
        @DependentPropertyNotNullOrEmpty(propertyName = "localNote", dependentPropertyName = "localLang")
})
@MinimumOnePropertyNotEmpty(propertyNames = { "additionalNotes", "foreignNote", "localNote" })
@Entity
@Table(name = "notes")
public class Note implements Serializable, HasOwner {

    private static final long serialVersionUID = -1700378910934447911L;

    private int noteId;
    private String additionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private Locale localLang;
    private String localNote;
    private String sourceUrl;
    private TranslationSource translationSource;
    private Page page;

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
    public int getNoteId() {

        return noteId;
    }

    @Transient
    @Override
    public String getOwnerUsername() {

        return getPage().getNotebook().getOwner().getUsername();
    }

    @ManyToOne
    @JoinColumn(name = "page")
    public Page getPage() {

        return page;
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

    public void setAdditionalNotes(String additionalNotes) {

        this.additionalNotes = additionalNotes;
    }

    public void setForeignLang(Locale foreignLang) {

        this.foreignLang = foreignLang;
    }

    public void setForeignNote(String foreignNote) {

        this.foreignNote = foreignNote;
    }

    public void setLocalLang(Locale localLang) {

        this.localLang = localLang;
    }

    public void setLocalNote(String localNote) {

        this.localNote = localNote;
    }

    public void setNoteId(int noteId) {

        this.noteId = noteId;
    }

    public void setPage(Page page) {

        this.page = page;
    }

    public void setSourceUrl(String sourceUrl) {

        this.sourceUrl = sourceUrl;
    }

    public void setTranslationSource(TranslationSource translationSource) {

        this.translationSource = translationSource;
    }
}
