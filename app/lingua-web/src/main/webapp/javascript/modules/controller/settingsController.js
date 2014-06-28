App.Controller.createNew(function() {

    this.isCalled("settingsController");
    
    this.imports("controller/abstractController");
    
    this.injects("$scope");
    
    this.hasDefinition(function() {

        return function($scope) {

            abstractController.setupDefaultScope($scope);
        };
    });
});
