App.Module.createNew(function() {

    this.isCalled("abstractController");
    
    this.hasDefinition(function() {

        var addNotebookChangedHandler = function(commsPipe, $scope) {
            
            $scope.func.currentNotebookChanged = function() {
                commsPipe.send(Components.READER, Components.ANY, Signals.CURRENT_NOTEBOOK_CHANGED);
            };
        };
        
        var setupDefaultScope = function($scope) {
            
            $scope.model = {};
            $scope.func = {};
            $scope.model.globalMessages = [];
        };
        
        var addCleanupStep = function($scope, func) {
            
            $scope.global.cleanupActions.push(func);
        };
        
        var setupCleanup = function($scope, $rootScope) {
            
            $rootScope.$on("$stateChangeSuccess", function() {
                $scope.global.doCleanup();
            });
        };
        
        return {
            addNotebookChangedHandler : addNotebookChangedHandler,
            setupDefaultScope : setupDefaultScope,
            setupCleanup : setupCleanup,
            addCleanupStep : addCleanupStep
        };
    });
});
