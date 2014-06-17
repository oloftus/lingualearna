(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var SettingsController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController("settingsController", [ "$scope", SettingsController ]);
    });
})();
