(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/translateController", "config/properties" ];

    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        linguaApp.value("translateServiceUrl", Properties.translateServiceUrl).value("languageNamesServiceUrl",
                Properties.languageNamesServiceUrl).value("sourceLang", initParams.sourceLang).value("targetLang",
                initParams.targetLang).value("initQuery", initParams.query);

        this.configure();
        this.boot();
    });
})();
