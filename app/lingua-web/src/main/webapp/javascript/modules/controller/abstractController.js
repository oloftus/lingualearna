(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        var AbstractController = {
            setupDefaultScope : function($scope) {
                
                $scope.model = {};
                $scope.func = {};
                $scope.model.globalMessages = [];
            }
        };
        
        return AbstractController;
    });
})();
