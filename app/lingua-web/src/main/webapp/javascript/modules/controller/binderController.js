(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var BinderController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController("binderController", [ "$scope", BinderController ]);
    });
})();
