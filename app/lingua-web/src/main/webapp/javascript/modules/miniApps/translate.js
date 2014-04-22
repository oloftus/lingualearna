(function() {

    loadRequireConfig();

    var dependencies = [ "angular", "linguaApp", "config/properties", "miniApps/abstractMiniApp", "underscore",
            "controller/translateController" ];

    define(dependencies, function(angular, linguaApp, properties, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        linguaApp.value("translateServiceUrl", properties.translateServiceUrl).value("languageNamesServiceUrl",
                properties.languageNamesServiceUrl).value("sourceLang", initParams.sourceLang).value("targetLang",
                initParams.targetLang).value("initQuery", initParams.query);

        boot();
    });
})();