(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector",
            "util/ngRegistrationHelper", "underscore", "localization/stringsDefault", "util/commsPipe",
            "service/notebookService", "util/messageHandler" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, ngRegistrationHelper, _) {

        var addNotebookChangedHandler = function(commsPipe, $scope) {

            $scope.func.currentNotebookChanged = function() {
                commsPipe.send(Components.READER, Components.ANY, Signals.CURRENT_NOTEBOOK_CHANGED);
            };
        };
        
        var ReaderBarController = function($scope, commsPipe, $state, notebookService, messageHandler) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            
            addNotebookChangedHandler(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("readerBarController",
                [ "$scope", "commsPipe", "$state", "notebookService", "messageHandler", ReaderBarController ]);
    });
})();
