App.Controller.createNew(function() {

    this.isCalled("settingsController");
    
    this.imports("controller/abstractController");
    this.imports("underscore");
    
    this.injects("$scope");
    
    this.hasDefinition(function(abstractController, _) {

        return function($scope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
        };
    });
});
