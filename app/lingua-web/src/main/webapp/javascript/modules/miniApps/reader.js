(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "jquery",
            "config/properties", "controller/pagesController" ];

    define(dependencies, function(linguaApp, abstractMiniApp, _, $) {

        _.extend(this, abstractMiniApp);

        linguaApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        linguaApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        linguaApp.constant("translateServiceUrl", Properties.translateServiceUrl);

        this.configure();
        this.boot();
    });
})();
