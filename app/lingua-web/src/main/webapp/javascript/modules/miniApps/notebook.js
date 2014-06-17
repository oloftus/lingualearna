(function() {

    loadRequireConfig();

    var imports = [ "linguaApp", "miniApps/abstractMiniApp", "controller/notebookController" ];

    define(imports, function(linguaApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        linguaApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        linguaApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        linguaApp.constant("translateServiceUrl", Properties.translateServiceUrl);
        linguaApp.constant("notesPagesServiceUrl", Properties.notesPagesServiceUrl);

        this.configure();
        this.boot();
    });
})();
