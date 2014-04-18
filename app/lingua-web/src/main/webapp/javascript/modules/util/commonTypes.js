define([], function() {

    TranslationRequest = function(sourceLang, targetLang, query) {

        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.query = query;
    };

    LanguageNameRequest = function(langCode) {

        this.langCode = langCode;
    };
    
    Note = function(foreignLang, foreignNote, localLang, localNote, additionalNotes, sourceUrl, noteId) {
        this.foreignLang = foreignLang;
        this.foreignNote = foreignNote;
        this.localLang = localLang;
        this.localNote = localNote;
        this.additionalNotes = additionalNotes;
        this.sourceUrl = additionalNotes;
        this.noteId = noteId;
    };

    HttpMethod = {
        POST: "POST",
        GET: "GET"
    };
    
    HttpHeaders = {
        BAD_REQUEST: 400
    };
    
    MessageSeverity = {
        INFO: "INFO",
        ERROR: "ERROR"
    };
});
