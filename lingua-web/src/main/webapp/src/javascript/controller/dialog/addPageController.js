define(
    [
        "module/ngModule",
        "types/types",
        "types/enums",
        "config/properties",
        "localization/strings",
        "underscore",
        "localization/strings",
        "service/pageService",
        "ui/notificationManager",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"
    ],
    function (ngModule, types, enums, properties, strings) {

        ngModule.controller("addPageController",
            [
                "$scope",
                "$timeout",
                "$state",
                "pageService",
                "notificationManager",
                "messageBus",
                "stateNavigator",
                "abstractController",

                function ($scope, $timeout, $state, pageService, NotificationHandler, messageBus, stateNavigator,
                          AbstractController) {

                    var notificationManager = new NotificationHandler($scope);
                    var abstractController = new AbstractController($scope);

                    function setupScope() {

                        $scope.model.pageName = null;
                    }

                    function setupClickHandlers() {

                        $scope.func.doCreatePage = function () {

                            var page = new types.Page($scope.model.pageName,
                                $scope.global.model.currentNotebook.notebookId);

                            function successHandler(page) {

                                notificationManager.newFormMessage(strings.pageCreatedMessage,
                                    enums.MessageSeverity.INFO);

                                messageBus.send(enums.Components.ADD_PAGE, enums.Components.ANY,
                                    enums.Signals.PAGE_SAVED_SUCCESS, page);

                                $timeout(function () {
                                    stateNavigator.goToParentState();
                                }, properties.dialogDisappearTimeout);
                            }

                            function errorHandler(data, status, headers) {

                                notificationManager.handleErrors(data, status, headers);
                            }

                            pageService.createPage(page, successHandler, errorHandler);
                        };
                    }

                    function construct() {

                        abstractController.setup();
                        setupScope();
                        setupClickHandlers();
                    }

                    construct();
                }
            ]);
    });