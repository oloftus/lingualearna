(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var HeaderController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            // TODO
        };

        ngRegistrationHelper(linguaApp).registerController("headerController", [ "$scope", HeaderController ]);
    });
})();
