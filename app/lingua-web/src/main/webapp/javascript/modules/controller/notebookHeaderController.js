App.Controller.createNew(function() {

    this.isCalled("notebookHeaderController");

    this.imports("controller/abstractController");
    
    this.injects("$scope");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController) {

        return function($scope, commsPipe) {

            abstractController.setupDefaultScope($scope);
            abstractController.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
