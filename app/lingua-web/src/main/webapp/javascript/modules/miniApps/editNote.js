(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/editNoteController", "config/properties" ];
    
    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        linguaApp
        .value("noteServiceUrl", Properties.noteServiceUrl)
        .value("languageNamesServiceUrl", Properties.languageNamesServiceUrl)
        .value("noteId", initParams.noteId);

        this.configure();
        this.boot();
    });
})();
