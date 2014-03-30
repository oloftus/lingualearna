package com.lingualearna.web.notes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "note")
public class Note implements Serializable {

	private static final long serialVersionUID = -1700378910934447911L;

	private int noteId;
	//	private Locale localLang;
	//	private Locale foreignLang;
	private String localNote;
	private String foreignNote;
	private String additionalNotes;

	//	private URL sourceUrl;
	//	private TranslationSource translationSource;

	@Id
	@Column(name = "note_id")
	public int getNoteId() {

		return noteId;
	}

	public void setNoteId(int noteId) {

		this.noteId = noteId;
	}

	@Column(name = "additional_notes")
	public String getAdditionalNotes() {

		return additionalNotes;
	}

	public void setAdditionalNotes(String additionalNotes) {

		this.additionalNotes = additionalNotes;
	}

	@Column(name = "local_note")
	public String getLocalNote() {

		return localNote;
	}

	public void setLocalNote(String localNote) {

		this.localNote = localNote;
	}

	@Column(name = "foreign_note")
	public String getForeignNote() {

		return foreignNote;
	}

	public void setForeignNote(String foreignNote) {

		this.foreignNote = foreignNote;
	}
}
