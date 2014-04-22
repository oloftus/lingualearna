(function() {
    
    loadRequireConfig();

    var dependencies = [ "linguaApp", "config/properties", "miniApps/abstractMiniApp", "underscore", "controller/addNoteController" ];
    
    define(dependencies, function(linguaApp, properties, abstractMiniApp, _) {

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

        linguaApp
        .value("noteServiceUrl", properties.noteServiceUrl)
        .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
        .value("formInput", formInput);

        boot();
    });
})();
