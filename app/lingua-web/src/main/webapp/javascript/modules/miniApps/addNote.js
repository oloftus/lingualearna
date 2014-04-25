(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/addNoteController", "config/properties" ];

    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

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
