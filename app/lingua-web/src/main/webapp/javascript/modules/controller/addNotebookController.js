App.Controller.createNew(function() {

    this.isCalled("addNotebookController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.loads("localization/stringsDefault");

    this.injects("$scope");
    this.injects("$timeout");
    this.injects("$state");
    this.injects("service/languageService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");

    this.hasDefinition(function(abstractController, _) {

        var setupScope = function($scope) {

            $scope.model.localLang = null;
            $scope.model.foreignLang = null;
            $scope.model.notebookName = null;
        };

        var setupLanguageDropdowns = function($scope, languageService, messageHandler) {

            languageService.getSupportedLanguages(function(supportedLanguages) {
                $scope.model.supportedLanguages = supportedLanguages;
            }, function(data, status, headers, config) {
                messageHandler.handleErrors($scope, data, status, headers);
            });
        };
        
        var addSubmitButtonHandler = function($scope, $timeout, $state, notebookService, messageHandler) {
            
            $scope.func.doCreateNotebook = function() {
                
                var foreignLang = !_.isNull($scope.model.foreignLang) ? $scope.model.foreignLang.langCode : null;
                var localLang = !_.isNull($scope.model.localLang) ? $scope.model.localLang.langCode : null;
                var notebook = new Notebook($scope.model.notebookName, foreignLang, localLang);
                
                var successHandler = function(data) {
                    
                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.notebookCreatedMessage,
                            MessageSeverity.INFO);
                    
                    $timeout(function() {
                        $state.go(AppStates.MAIN);
                    }, App.Properties.dialogDisappearTimeout);
                };
                
                var errorHandler = function(data, status, headers, config) {
                    
                    messageHandler.handleErrors($scope, data, status, headers);
                };
                
                notebookService.create(notebook, successHandler, errorHandler);
            };
        };

        return function($scope, $timeout, $state, languageService, notebookService, messageHandler) {

            abstractController.setupDefaultScope($scope);
            setupScope($scope);
            setupLanguageDropdowns($scope, languageService, messageHandler);
            addSubmitButtonHandler($scope, $timeout, $state, notebookService, messageHandler);
        };
    });
});
