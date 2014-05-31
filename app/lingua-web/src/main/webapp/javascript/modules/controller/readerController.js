(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector",
            "util/ngRegistrationHelper", "underscore", "localization/stringsDefault", "util/commsPipe",
            "service/notebookService", "util/messageHandler" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, ngRegistrationHelper, _) {

        var subscribeToCurrentNotebookChangedEvents = function(commsPipe, $scope) {

            $scope.func.currentNotebookChanged = function() {
                commsPipe.send(Components.READER, Components.ANY, Signals.CurrentNotebookChanged);
            };
        };
        
        var ReaderController = function($scope, commsPipe, $state, notebookService, messageHandler) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            
            subscribeToCurrentNotebookChangedEvents(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("readerController",
                [ "$scope", "commsPipe", "$state", "notebookService", "messageHandler", ReaderController ]);
    });
})();
