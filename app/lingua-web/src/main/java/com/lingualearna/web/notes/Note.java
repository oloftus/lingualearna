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
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@DependentPropertyNotNullOrEmpty.Properties({
        @DependentPropertyNotNullOrEmpty(propertyName = "foreignNote", dependentPropertyName = "foreignLang"),
        @DependentPropertyNotNullOrEmpty(propertyName = "localNote", dependentPropertyName = "localLang")
})
@Entity
@Table(name = "notes")
public class Note implements Serializable {

    private static final long serialVersionUID = -1700378910934447911L;

    private int noteId;
    private String additionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private Locale localLang;
    private String localNote;
    private String sourceUrl;
    private TranslationSource translationSource;

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

    public void setSourceUrl(String sourceUrl) {

        this.sourceUrl = sourceUrl;
    }

    public void setTranslationSource(TranslationSource translationSource) {

        this.translationSource = translationSource;
    }
}
