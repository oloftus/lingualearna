App.Controller.createNew(function() {

    this.isCalled("settingsController");
    
    this.imports("rootApp");
    this.imports("controller/abstractController");
    this.imports("underscore");
    
    this.dependsOnNg("$scope");
    
    this.hasDefinition(function(rootApp, abstractController, _) {

        return function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };
    });
});
