App.Controller.createNew(function() {

    this.isCalled("settingsController");
    
    this.injects("$scope");
    
    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        return function($scope) {

            this.setupDefaultScope($scope);
        };
    });
});
