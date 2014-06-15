(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var HeaderController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController("headerController", [ "$scope", HeaderController ]);
    });
})();
