loadRequireConfig();

(function() {
    
    loadRequireConfig();

    var dependencies = [ "linguaApp", "underscore" ];
    
    define(dependencies, function(linguaApp, _) {

        _.extend(this, abstractMiniApp);

        

        boot();
    });
})();
