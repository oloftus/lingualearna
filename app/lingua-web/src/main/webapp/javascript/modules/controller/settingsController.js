(function() {

    var componentName = "settingsController";
    
    var imports = [];
    var ngDependencies = [];
    
    imports.push("linguaApp");
    imports.push("controller/abstractController");
    imports.push("util/ngRegistrationHelper");
    imports.push("underscore");
    
    ngDependencies.push("$scope");
    
    define(doImport(imports), function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var SettingsController = function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };

        ngRegistrationHelper(linguaApp).registerController(componentName, ngDependencies, SettingsController);
    });
})();
