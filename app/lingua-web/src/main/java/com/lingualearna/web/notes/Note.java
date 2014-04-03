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

@DependentFieldNotNull.List({
		@DependentFieldNotNull(fieldName = "foreignNote", dependentFieldName = "foreignLang"),
		@DependentFieldNotNull(fieldName = "localNote", dependentFieldName = "localLang")
})
@Entity(name = "note")
public class Note implements Serializable {

	private static final long serialVersionUID = -1700378910934447911L;

	private String additionalNotes;
	private Locale foreignLang;
	private String foreignNote;
	private Locale localLang;
	private String localNote;
	private int noteId;
	private String sourceUrl;
	private TranslationSource translationSource;

	@Column(name = "additional_notes")
	public String getAdditionalNotes() {

		return additionalNotes;
	}

	@Column(name = "foreign_lang")
	public Locale getForeignLang() {

		return foreignLang;
	}

	@Column(name = "foreign_note")
	public String getForeignNote() {

		return foreignNote;
	}

	@Column(name = "local_lang")
	public Locale getLocalLang() {

		return localLang;
	}

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

	@Column(name = "source_url")
	public String getSourceUrl() {

		return sourceUrl;
	}

	@Column(name = "translation_source")
	@Enumerated(EnumType.STRING)
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
