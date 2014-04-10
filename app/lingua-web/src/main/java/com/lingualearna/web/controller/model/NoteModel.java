package com.lingualearna.web.controller.model;

import java.io.Serializable;
import java.util.Locale;

public class NoteModel implements Serializable {

    private static final long serialVersionUID = 6666949136391165628L;

    private String additionalNotes;
    private Locale foreignLang;
    private String foreignNote;
    private Locale localLang;
    private String localNote;
    private int noteId;
    private String sourceUrl;

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

    public int getNoteId() {

        return noteId;
    }

    public String getSourceUrl() {

        return sourceUrl;
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
}
