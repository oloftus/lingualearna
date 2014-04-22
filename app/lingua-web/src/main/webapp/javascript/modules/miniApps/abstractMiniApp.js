(function() {
    
    var dependencies = [ "angular" ];
    
    define(dependencies, function(angular) {

        var AbstractMiniApp = {
            
                boot : function() {
                    angular.bootstrap(document, [ "linguaAppx" ]);
                }
        };
        
        return AbstractMiniApp;
    });
})();
