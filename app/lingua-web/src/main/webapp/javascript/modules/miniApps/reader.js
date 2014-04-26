(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/pagesController" ];

    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        var initParams = {
                foreignLang : "en",
                foreignNote : "",
                localLang : "de",
                localNote : "",
                additionalNotes : "",
                sourceUrl : "",
                noteId : ""
        };
        
        var formInput = {
                foreignLang : initParams.foreignLang,
                localLang : initParams.localLang,
                foreignNote : initParams.foreignNote,
                localNote : initParams.localNote,
                additionalNotes : initParams.additionalNotes,
                sourceUrl : initParams.sourceUrl,
                noteId : initParams.noteId
            };

            linguaApp.value("noteServiceUrl", Properties.noteServiceUrl).value("languageNamesServiceUrl",
                    Properties.languageNamesServiceUrl).value("formInput", formInput);

        this.configure();
        this.boot();
    });
})();
