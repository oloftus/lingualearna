define(
    [
        "module/ngModule",
        "types/enums",
        "types/types",
        "config/properties",
        "localization/strings",
        "underscore",
        "service/languageService",
        "service/notebookService",
        "ui/notificationManager",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"
    ],
    function (ngModule, enums, types, properties, strings, _) {

        ngModule.controller("addNotebookController",
            [
                "$scope",
                "$timeout",
                "$state",
                "languageService",
                "notebookService",
                "notificationManager",
                "messageBus",
                "stateNavigator",
                "abstractController",

                function ($scope, $timeout, $state, languageService, notebookService, NotificationHandler, messageBus,
                          stateNavigator, AbstractController) {

                    var notificationManager = new NotificationHandler($scope);
                    var abstractController = new AbstractController($scope);

                    function setupScope() {

                        $scope.model.localLang = null;
                        $scope.model.foreignLang = null;
                        $scope.model.notebookName = null;
                    }

                    function setupLanguageDropdowns() {

                        languageService.getSupportedLanguages(function (supportedLanguages) {
                            $scope.model.supportedLanguages = supportedLanguages;
                        }, function (data, status, headers) {
                            notificationManager.handleErrors(data, status, headers);
                        });
                    }

                    function setupClickHandlers() {

                        $scope.func.doCreateNotebook = function () {

                            var foreignLang = !_.isNull($scope.model.foreignLang) ? $scope.model.foreignLang.langCode : null;
                            var localLang = !_.isNull($scope.model.localLang) ? $scope.model.localLang.langCode : null;
                            var notebook = new types.Notebook($scope.model.notebookName, foreignLang, localLang);

                            function successHandler(notebook) {

                                notificationManager.newFormMessage(strings.notebookCreatedMessage,
                                    enums.MessageSeverity.INFO);

                                messageBus.send(enums.Components.ADD_NOTEBOOK, enums.Components.ANY,
                                    enums.Signals.NOTEBOOK_SAVED_SUCCESS, notebook);

                                $timeout(function () {
                                    stateNavigator.goToParentState();
                                }, properties.dialogDisappearTimeout);
                            }

                            function errorHandler(data, status, headers) {

                                notificationManager.handleErrors(data, status, headers);
                            }

                            notebookService.createNotebook(notebook, successHandler, errorHandler);
                        };
                    }

                    function construct() {

                        abstractController.setup();
                        setupScope();
                        setupLanguageDropdowns();
                        setupClickHandlers();
                    }

                    construct();
                }
            ]);
    });
