(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        TranslationRequest = function(sourceLang, targetLang, query) {

            this.sourceLang = sourceLang;
            this.targetLang = targetLang;
            this.query = query;
        };

        LanguageNameRequest = function(langCode) {

            this.langCode = langCode;
        };

        Note = function(foreignLang, foreignNote, localLang, localNote, additionalNotes, sourceUrl, translationSource,
                pageId, noteId) {

            this.foreignLang = foreignLang;
            this.foreignNote = foreignNote;
            this.localLang = localLang;
            this.localNote = localNote;
            this.additionalNotes = additionalNotes;
            this.sourceUrl = sourceUrl;
            this.noteId = noteId;
            this.translationSource = translationSource;
            this.pageId = pageId;
        };

        IllegalArgumentException = function(message) {
            this.message = message;
        };
    });
})();
