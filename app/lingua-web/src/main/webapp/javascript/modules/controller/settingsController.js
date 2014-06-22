App.Controller.createNew(function() {

    this.moduleIsCalled("settingsController");
    
    this.imports("linguaApp");
    this.imports("controller/abstractController");
    this.imports("util/ngRegistrationHelper");
    this.imports("underscore");
    
    this.dependsOnNg("$scope");
    
    this.hasDefinition(function(linguaApp, abstractController, ngRegistrationHelper, _) {

        return function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };
    });
});
