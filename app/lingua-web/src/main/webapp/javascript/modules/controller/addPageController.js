App.Controller.createNew(function() {

    this.isCalled("addPageController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.loads("localization/stringsDefault");

    this.injects("$scope");
    this.injects("$timeout");
    this.injects("$state");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController, _) {

        var setupScope = function($scope) {

            $scope.model.pageName = null;
        };

        var addSubmitButtonHandler = function($scope, $timeout, $state, notebookService, messageHandler, commsPipe) {
            
            $scope.func.doCreatePage = function() {
                
                var page = new Page($scope.model.pageName, $scope.global.model.currentNotebook.notebookId);
                
                var successHandler = function(page) {
                    
                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.pageCreatedMessage,
                            MessageSeverity.INFO);
                    
                    commsPipe.send(Components.ADD_PAGE, Components.ANY, Components.PAGE_SAVED_SUCCESS, page);
                    
                    $timeout(function() {
                        $state.go(AppStates.MAIN);
                    }, App.Properties.dialogDisappearTimeout);
                };
                
                var errorHandler = function(data, status, headers, config) {
                    
                    messageHandler.handleErrors($scope, data, status, headers);
                };
                
                notebookService.createPage(page, successHandler, errorHandler);
            };
        };

        return function($scope, $timeout, $state, notebookService, messageHandler, commsPipe) {

            abstractController.setupDefaultScope($scope);
            setupScope($scope);
            addSubmitButtonHandler($scope, $timeout, $state, notebookService, messageHandler, commsPipe);
        };
    });
});
