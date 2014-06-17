(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

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
        
        var AbstractController = {
            addNotebookChangedHandler : addNotebookChangedHandler,
            setupDefaultScope : setupDefaultScope
        };
        
        return AbstractController;
    });
})();
