(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector",
            "util/ngRegistrationHelper", "underscore", "util/commsPipe", "service/notebookService" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, ngRegistrationHelper, _) {

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var message = new TranslationRequest($scope.global.model.currentNotebook.sourceLang,
                        $scope.global.model.currentNotebook.targetLang, selected);

                $state.go(AppStates.TRANSLATE).then(function() {
                    commsPipe.send(Components.READER, Components.TRANSLATE, message);
                });

                textSelector.clearSelected();
            }
        };
        
        var setupOneClickTranslation = function(commsPipe, $state, $scope) {
            
            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });
        };
        
        var setupNotebookEnvironment = function($scope, notebookService) {
            
            notebookService.getListOfNotebooks(function(data) {

                $scope.global.model.notebooks = data;
                
                _.some($scope.global.model.notebooks, function(notebook) {
                    if (notebook.lastUsed) {
                        $scope.global.model.currentNotebook = {};
                        $scope.global.model.currentNotebook.url = notebook.url;
                        $scope.global.model.currentNotebook.name = notebook.name;
                        $scope.global.model.currentNotebook.sourceLang = notebook.foreignLang;
                        $scope.global.model.currentNotebook.targetLang = notebook.localLang;
                        
                        return true;
                    }
                    
                    return false;
                });
                
            }, function() {
                addFreshGlobalMessage($scope, LocalStrings.genericServerErrorMessage, MessageSeverity.ERROR);
            });
        };

        var ReaderController = function($scope, commsPipe, $state, notebookService) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            setupOneClickTranslation(commsPipe, $state, $scope);
            setupNotebookEnvironment($scope, notebookService);
        };

        ngRegistrationHelper(linguaApp).registerController("readerController",
                [ "$scope", "commsPipe", "$state", "notebookService", ReaderController ]);
    });
})();
