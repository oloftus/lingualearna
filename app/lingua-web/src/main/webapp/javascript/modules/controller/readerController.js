(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, _) {

        var readerController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            
            
        };

        linguaApp.controller("readerController", [ "$scope", readerController ]);
    });
})();
