App.TypesDef.createNew(function() {

    TranslationRequest = function(sourceLang, targetLang, query) {

        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.query = query;
    };

    LanguageNameRequest = function(langCode) {

        this.langCode = langCode;
    };

    Note = function(foreignLang, foreignNote, localLang, localNote, additionalNotes, sourceUrl, translationSource,
            includedInTest, pageId, noteId) {

        this.foreignLang = foreignLang;
        this.foreignNote = foreignNote;
        this.localLang = localLang;
        this.localNote = localNote;
        this.additionalNotes = additionalNotes;
        this.sourceUrl = sourceUrl;
        this.translationSource = translationSource;
        this.includedInTest = includedInTest;
        this.pageId = pageId;
        this.noteId = noteId;
    };

    Notebook = function(name, foreignLang, localLang, notebookId) {

        this.name = name;
        this.foreignLang = foreignLang;
        this.localLang = localLang;
        this.notebookId = notebookId;
    };

    Page = function(name, notebookId, pageId, position) {

        this.name = name;
        this.notebookId = notebookId;
        this.pageId = pageId;
        this.position = position;
    };

    IllegalArgumentException = function(message) {
        this.message = message;
    };
});
