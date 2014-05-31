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
                includeInTest, pageId, noteId) {

            this.foreignLang = foreignLang;
            this.foreignNote = foreignNote;
            this.localLang = localLang;
            this.localNote = localNote;
            this.additionalNotes = additionalNotes;
            this.sourceUrl = sourceUrl;
            this.translationSource = translationSource;
            this.includeInTest = includeInTest;
            this.pageId = pageId;
            this.noteId = noteId;
        };

        IllegalArgumentException = function(message) {
            this.message = message;
        };
    });
})();
