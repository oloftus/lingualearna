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
        
        return {
            addNotebookChangedHandler : addNotebookChangedHandler,
            setupDefaultScope : setupDefaultScope
        };
    });
});
