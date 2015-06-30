package com.lingualearna.web.notes;

import java.io.Serializable;
import java.util.Locale;

import javax.validation.constraints.Min;

public class NoteModel implements Serializable {

    private static final long serialVersionUID = 6666949136391165628L;

    private int pageId;
    private String additionalNotes;
    private String flatAdditionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private String flatForeignNote;
    private Locale localLang;
    private String localNote;
    private String flatLocalNote;
    private Integer noteId;
    private String sourceUrl;
    private Integer position;
    private TranslationSource translationSource;
    private boolean includedInTest;
    private boolean starred;
    private String sourceContext;

    public String getSourceContext() {

        return sourceContext;
    }

    public void setSourceContext(String sourceContext) {

        this.sourceContext = sourceContext;
    }

    public String getFlatForeignNote() {

        return flatForeignNote;
    }

    public void setFlatForeignNote(String flatForeignNote) {

        this.flatForeignNote = flatForeignNote;
    }

    public String getFlatLocalNote() {

        return flatLocalNote;
    }

    public void setFlatLocalNote(String flatLocalNote) {

        this.flatLocalNote = flatLocalNote;
    }

    public String getFlatAdditionalNotes() {

        return flatAdditionalNotes;
    }

    public void setFlatAdditionalNotes(String flatAdditionalNotes) {

        this.flatAdditionalNotes = flatAdditionalNotes;
    }

    public String getAdditionalNotes() {

        return additionalNotes;
    }

    public Locale getForeignLang() {

        return foreignLang;
    }

    public String getForeignNote() {

        return foreignNote;
    }

    public Locale getLocalLang() {

        return localLang;
    }

    public String getLocalNote() {

        return localNote;
    }

    public Integer getNoteId() {

        return noteId;
    }

    @Min(value = 1, message = "Select a page to save the note to")
    public int getPageId() {

        return pageId;
    }

    public Integer getPosition() {

        return position;
    }

    public String getSourceUrl() {

        return sourceUrl;
    }

    public TranslationSource getTranslationSource() {

        return translationSource;
    }

    public boolean isIncludedInTest() {

        return includedInTest;
    }

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

    public void setPageId(int pageId) {

        this.pageId = pageId;
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
