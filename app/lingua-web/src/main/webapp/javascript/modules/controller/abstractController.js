(function() {

    var dependencies = [];

    define(dependencies, function() {

        var AbstractController = {
                
            setupDefaultScope : function(scope) {

                scope.model = {};
                scope.func = {};
                scope.model.globalMessages = [];
            }
        };
        
        return AbstractController;
    });
})();
