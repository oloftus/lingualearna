(function() {
    
    loadRequireConfig();

    var dependencies = [ "linguaApp", "miniApps/abstractMiniApp", "underscore", "controller/readerController" ];
    
    define(dependencies, function(linguaApp, abstractMiniApp, _) {

        _.extend(this, abstractMiniApp);


        boot();
    });
})();
