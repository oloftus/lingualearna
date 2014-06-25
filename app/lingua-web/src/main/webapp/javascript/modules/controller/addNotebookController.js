App.Controller.createNew(function() {

    this.isCalled("addNotebookController");

    this.imports("underscore");

    this.loads("localization/stringsDefault");

    this.injects("$scope");
    this.injects("service/languageNamesService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");

    this.extends("controller/abstractController");

    this.hasDefinition(function(_) {

        var setupScope = function($scope) {

            $scope.model.localLang = null;
            $scope.model.foreignLang = null;
            $scope.model.notebookName = null;
        };

        var setupLanguageDropdowns = function($scope, languageNamesService, messageHandler) {

            languageNamesService.getSupportedLanguages(function(data) {
                $scope.model.supportedLanguages = data;
            }, function(data, status, headers, config) {
                messageHandler.handleErrors($scope, data, status, headers);
            });
        };
        
        var addSubmitButtonHandler = function($scope, notebookService, messageHandler) {
            
            $scope.func.doCreateNotebook = function() {
                
                var foreignLang = !_.isNull($scope.model.foreignLang) ? $scope.model.foreignLang.langCode : null;
                var localLang = !_.isNull($scope.model.localLang) ? $scope.model.localLang.langCode : null;
                var notebook = new Notebook($scope.model.notebookName, foreignLang, localLang);
                
                notebookService.create(notebook, function(data) {
                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.notebookCreatedMessage,
                            MessageSeverity.INFO);
                }, function(data, status, headers, config) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };
        };

        return function($scope, languageNamesService, notebookService, messageHandler) {

            this.setupDefaultScope($scope);
            setupScope($scope);
            setupLanguageDropdowns($scope, languageNamesService, messageHandler);
            addSubmitButtonHandler($scope, notebookService, messageHandler);
        };
    });
});
