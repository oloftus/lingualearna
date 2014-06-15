(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "controller/notebookController" ];

    define(dependencies, function(linguaApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        this.configure();
        this.boot();
    });
})();
