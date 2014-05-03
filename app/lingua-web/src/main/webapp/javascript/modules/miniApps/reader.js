(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "controller/readerController" ];

    define(dependencies, function(linguaApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        linguaApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        linguaApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        linguaApp.constant("translateServiceUrl", Properties.translateServiceUrl);

        this.configure();
        this.boot();
    });
})();
