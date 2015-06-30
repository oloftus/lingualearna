package com.lingualearna.web.knowledgetest;

import java.io.Serializable;

public class TestEntryModel implements Serializable {

    private static final long serialVersionUID = -2060382930389645786L;

    private String foreignNote;
    private String localNote;
    private Integer noteId;
    private boolean hasSourceContext;

    public boolean getHasSourceContext() {

        return hasSourceContext;
    }

    public void setHasSourceContext(boolean hasSourceContext) {

        this.hasSourceContext = hasSourceContext;
    }

    public Integer getNoteId() {

        return noteId;
    }

    public String getForeignNote() {

        return foreignNote;
    }

    public String getLocalNote() {

        return localNote;
    }

    public void setNoteId(Integer entryId) {

        this.noteId = entryId;
    }

    public void setForeignNote(String foreignNote) {

        this.foreignNote = foreignNote;
    }

    public void setLocalNote(String localNote) {

        this.localNote = localNote;
    }
}
