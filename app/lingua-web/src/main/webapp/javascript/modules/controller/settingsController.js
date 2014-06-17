(function() {

    var imports = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore" ];

    define(imports, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var SettingsController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController("settingsController", [ "$scope", SettingsController ]);
    });
})();
