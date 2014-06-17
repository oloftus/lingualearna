(function() {

    var imports = [ "linguaApp", "util/ngRegistrationHelper", "controller/abstractMiniAppController",
            "util/textSelector", "util/appStates", "underscore", "controller/readerBarController",
            "service/jsonWebService", "util/messageHandler", "util/commsPipe", "service/notebookService" ];

    define(imports, function(linguaApp, ngRegistrationHelper, abstractMiniAppController, textSelector, appStates, _) {

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var translationRequest = new TranslationRequest($scope.global.model.currentNotebook.localLang,
                        $scope.global.model.currentNotebook.foreignLang, selected);

                $state.go(AppStates.TRANSLATE).then(
                        function() {
                            commsPipe.send(Components.READER, Components.TRANSLATE, Subjects.TRANSLATION_REQUEST,
                                    translationRequest);
                        });

                textSelector.clearSelected();
            }
        };

        var setupClickToTranslate = function(commsPipe, $state, $scope) {

            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });
        };

        var getCsrfAndTriggerLogin = function(jsonWebService) {

            jsonWebService.getCsrfToken();
        };

        var ReaderController = function($scope, $state, jsonWebService, $timeout, messageHandler, notebookService,
                commsPipe) {

            _.extend(this, abstractMiniAppController);
            appStates.setMainState(AppStates.READER_MAIN);
            this.setupGlobalScope($scope, $state);
            this.setupPageMessages($scope, messageHandler, $timeout);
            this.setupDialogs($scope);
            this.setupSpecialDialogs($scope);
            this.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            this.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);

            getCsrfAndTriggerLogin(jsonWebService, $scope);
            setupClickToTranslate(commsPipe, $state, $scope);

            $state.go(AppStates.READER_MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController(
                "readerController",
                [ "$scope", "$state", "jsonWebService", "$timeout", "messageHandler", "notebookService", "commsPipe",
                        ReaderController ]);
    });
})();
