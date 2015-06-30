define({

    TranslationRequest: function (sourceLang, targetLang, query, sourceContext) {

        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.query = query;
        this.sourceContext = sourceContext;
    },

    LanguageNameRequest: function (langCode) {

        this.langCode = langCode;
    },

    Note: function (foreignLang, foreignNote, localLang, localNote, additionalNotes, sourceUrl, sourceContext,
                    translationSource, includedInTest, starred, pageId, noteId, position) {

        this.foreignLang = foreignLang;
        this.foreignNote = foreignNote;
        this.localLang = localLang;
        this.localNote = localNote;
        this.additionalNotes = additionalNotes;
        this.sourceUrl = sourceUrl;
        this.sourceContext = sourceContext;
        this.translationSource = translationSource;
        this.includedInTest = includedInTest;
        this.starred = starred;
        this.pageId = pageId;
        this.noteId = noteId;
        this.position = position;
    },

    Notebook: function (name, foreignLang, localLang, notebookId) {

        this.name = name;
        this.foreignLang = foreignLang;
        this.localLang = localLang;
        this.notebookId = notebookId;
    },

    Page: function (name, notebookId, pageId, position) {

        this.name = name;
        this.notebookId = notebookId;
        this.pageId = pageId;
        this.position = position;
    }
});
