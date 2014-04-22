(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "config/properties", "miniApps/abstractMiniApp", "underscore", "controller/editNoteController", ];
    
    define(dependencies, function(linguaApp, properties, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        linguaApp
        .value("noteServiceUrl", properties.noteServiceUrl)
        .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
        .value("noteId", initParams.noteId);

        boot();
    });
})();
