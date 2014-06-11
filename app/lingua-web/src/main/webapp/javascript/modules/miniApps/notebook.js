(function() {

    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp" ];

    define(dependencies, function(linguaApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        console.log("booting");

        this.configure();
        this.boot();
    });
})();
