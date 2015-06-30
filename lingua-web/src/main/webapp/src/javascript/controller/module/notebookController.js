define(
    [
        "module/ngModule",
        "types/enums",
        "config/properties",
        "controller/panel/notebookHeaderController",
        "controller/panel/binderController",
        "controller/dialog/addNoteController",
        "controller/dialog/editNoteController",
        "controller/dialog/translateController",
        "controller/dialog/loginController",
        "controller/dialog/settingsController",
        "controller/dialog/addNotebookController",
        "controller/dialog/addPageController",
        "controller/panel/noteListController",
        "controller/panel/pageListController",
        "controller/panel/knowledgeTestController",
        "controller/module/moduleController",
        "service/jsonWebService",
        "directive/lgRelativeSref",
        "state/stateNavigator",
        "utility/messageBus"
    ],
    function (ngModule, enums, properties) {

        ngModule.controller("notebookController",
            [
                "$scope",
                "jsonWebService",
                "moduleController",
                "stateNavigator",
                "messageBus",

                function ($scope, jsonWebService, ModuleController, stateNavigator, messageBus) {

                    var moduleController = new ModuleController($scope);

                    function construct() {

                        moduleController.setup();
                        jsonWebService.setCsrfToken(properties.csrfToken);
                        stateNavigator.setStartState(enums.AppStates.NOTEBOOK);
                        stateNavigator.goToState(enums.AppStates.START);

                        function setupNotebookDataManager() {
                            moduleController.setupNotebookDataManager();
                        }

                        messageBus.subscribe(enums.Components.BINDER, enums.Components.NOTEBOOK,
                            setupNotebookDataManager, enums.Signals.DONE_SETUP);
                    }

                    construct();
                }
            ]);
    });
