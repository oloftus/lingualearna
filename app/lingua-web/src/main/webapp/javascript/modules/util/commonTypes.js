(function() {

    var dependencies = [ "config/properties" ];

    define(dependencies, function() {

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
            this.sourceUrl = sourceUrl;
            this.noteId = noteId;
        };
        
        IllegalArgumentException = function(message) {
            this.message = message;
        };

        HttpMethod = {
            POST : "POST",
            GET : "GET",
            PUT : "PUT",
            DELETE : "DELETE"
        };

        HttpHeaders = {
            BAD_REQUEST : 400
        };

        MessageSeverity = {
            INFO : "INFO",
            ERROR : "ERROR"
        };
        
        Dialogs = {
            ADD_NOTE : "ADD_NOTE"
        };
        
        AppStates = {
            MAIN : "MAIN",
            ADD_NOTE : "ADD_NOTE",
            TRANSLATE : "TRANSLATE"
        };
    });
})();
