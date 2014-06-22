App.Controller.createNew(function() {

    this.moduleIsCalled("notebookHeaderController");

    this.imports("linguaApp");
    this.imports("controller/abstractController");
    this.imports("util/ngRegistrationHelper");
    this.imports("underscore");

    this.importsNg("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(linguaApp, abstractController, ngRegistrationHelper, _) {

        return function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
