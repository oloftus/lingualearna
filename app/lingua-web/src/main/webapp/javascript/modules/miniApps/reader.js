(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/pagesController" ];

    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);

        linguaApp
        .constant("noteServiceUrl", Properties.noteServiceUrl)
        .constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);

        this.configure();
        this.boot();
    });
})();
