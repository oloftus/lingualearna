(function() {

    var componentName = "notebookHeaderController";

    var imports = [];
    var ngImports = [];
    var ngDependencies = [];

    imports.push("linguaApp");
    imports.push("controller/abstractController");
    imports.push("util/ngRegistrationHelper");
    imports.push("underscore");

    ngImports.push("util/commsPipe");

    ngDependencies.push("$scope");
    ngDependencies.push("commsPipe");

    define(doImport(imports, ngImports), function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var NotebookHeaderController = function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController(componentName, ngDependencies, NotebookHeaderController);
    });
})();
