(function() {

    var imports = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore" ];

    define(imports, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var BinderController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController("binderController", [ "$scope", BinderController ]);
    });
})();
