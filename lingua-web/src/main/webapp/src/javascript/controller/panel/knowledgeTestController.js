define(
    [
        "module/ngModule",
        "types/enums",
        "localization/strings",
        "underscore",
        "directive/lgTestEntry",
        "service/knowledgeTestService",
        "ui/notificationManager",
        "controller/abstractController",
        "state/stateNavigator"
    ],
    function (ngModule, enums, strings, _) {

        ngModule.controller("knowledgeTestController",
            [
                "$scope",
                "knowledgeTestService",
                "notificationManager",
                "abstractController",
                "stateNavigator",

                function ($scope, knowledgeTestService, NotificationHandler, AbstractController, stateNavigator) {

                    var notificationManager = new NotificationHandler($scope);
                    var abstractController = new AbstractController($scope);

                    function loadTestEntries() {

                        function successHandler(testEntries) {

                            $scope.model.testEntries = [];

                            _.each(testEntries, function (testEntry) {
                                testEntry.hidden = true;
                                $scope.model.testEntries.push(testEntry);
                            });
                        }

                        function failureHandler() {

                            notificationManager.newPageMessage(strings.genericServerErrorMessage,
                                enums.MessageSeverity.ERROR);
                        }

                        knowledgeTestService.getRandomTestEntries($scope.global.model.currentNotebook.notebookId,
                            successHandler, failureHandler);
                    }

                    function getContext(entry) {

                        function successHandler(sourceContext) {

                            $scope.model.sourceContext = sourceContext;
                            stateNavigator.goToRelativeState(enums.AppStates.KNOWLEDGE_TEST_HINT);
                        }

                        function failureHandler() {

                            notificationManager.newPageMessage(strings.getSourceContextError,
                                enums.MessageSeverity.ERROR);
                        }

                        knowledgeTestService.getSourceContext(entry.noteId, successHandler, failureHandler);
                    }

                    function setupClickHandlers() {

                        $scope.func.nextTest = function () {

                            loadTestEntries();
                        };

                        $scope.func.reveal = function (testEntry) {

                            testEntry.hidden = !testEntry.hidden;
                        };

                        $scope.func.getContext = getContext;
                    }

                    function construct() {

                        abstractController.setup();
                        setupClickHandlers();
                        loadTestEntries();
                    }

                    construct();
                }
            ])
    });
