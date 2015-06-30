define(
    [
        "module/ngModule",
        "types/enums",
        "config/properties",
        "ui/dialogsManager",
        "utility/cleanupManager",
        "state/stateNavigator",
        "data/notebookDataManager"
    ],
    function (ngModule, enums, properties, DialogsManager, CleanupManager) {

        ngModule.factory("moduleController",
        [
            "$rootScope",
            "$timeout",
            "stateNavigator",
            "notebookService",
            "messageBus",
            "notificationManager",
            "notebookDataManager",

            function($rootScope, $timeout, stateNavigator, notebookService, messageBus, NotificationHandler, NotebookEnvironmentManager) {

                return function($scope) {

                    var notificationManager = new NotificationHandler($scope);
                    var dialogsManager = new DialogsManager($scope);
                    var notebookDataManager = new NotebookEnvironmentManager($scope);
                    var cleanupManager = new CleanupManager($scope);

                    function setupGlobalScope() {

                        $scope.global = {};
                        $scope.global.model = {};
                        $scope.global.func = {};

                        $scope.global.model.pageMessages = [];
                        $scope.global.properties = properties;

                        $scope.global.cleanupActions = [];
                    }

                    function setup() {

                        setupGlobalScope();
                        notificationManager.setup();
                        dialogsManager.setup();
                        cleanupManager.setup($rootScope);
                    }

                    function setupNotebookDataManager() {

                        notebookDataManager.setup();
                    }

                    function doExport() {

                        this.setup = setup;
                        this.setupNotebookDataManager = setupNotebookDataManager;
                    }

                    doExport.call(this);
                }
            }
        ])
    });
