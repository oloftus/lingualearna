App.Controller.createNew(function() {

    this.isCalled("binderController");

    this.injects("$scope");
    
    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        return function($scope) {
            this.setupDefaultScope($scope);
        };
    });
});
