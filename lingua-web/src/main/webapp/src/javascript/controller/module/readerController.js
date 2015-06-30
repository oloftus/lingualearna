define(
    [
        "module/ngModule",
        "types/types",
        "types/enums",
        "ui/textSelector",
        "underscore",
        "jquery",
        "underscore.string",
        "controller/dialog/addNoteController",
        "controller/dialog/addNotebookController",
        "controller/dialog/translateController",
        "controller/dialog/loginController",
        "service/jsonWebService",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/module/moduleController",
        "directive/lgRelativeSref",
        "directive/lgCurrentNotebookSelector"
    ],
    function (ngModule, types, enums, textSelector, _, $) {

        ngModule.controller("readerController",
            [
                "$scope",
                "$timeout",
                "jsonWebService",
                "messageBus",
                "moduleController",
                "stateNavigator",

                function ($scope, $timeout, jsonWebService, messageBus, ModuleController, stateNavigator) {

                    var moduleController = new ModuleController($scope);

                    function mouseupHandler() {

                        var selected = textSelector.getSelected();
                        var selectedText = selected.selectedText;

                        if (!_.isEmpty(_.trim(selectedText))) {
                            var translationRequest = new types.TranslationRequest($scope.global.model.currentNotebook.foreignLang,
                                $scope.global.model.currentNotebook.localLang, selectedText, selected.contextText);

                            stateNavigator.goToRelativeState(enums.AppStates.TRANSLATE).then(function () {
                                messageBus.send(enums.Components.READER, enums.Components.TRANSLATE, enums.Subjects.TRANSLATION_REQUEST,
                                    translationRequest);
                            });

                            textSelector.clearSelected();
                        }
                    }

                    function setupClickToTranslate() {

                        $(document).bind("mouseup", function () {
                            mouseupHandler();
                        });
                    }

                    function getCsrfAndTriggerLogin() {

                        jsonWebService.acquireCsrfToken();
                    }

                    function construct() {

                        moduleController.setup();
                        moduleController.setupNotebookDataManager();
                        setupClickToTranslate();
                        stateNavigator.setStartState(enums.AppStates.READER);
                        stateNavigator.goToState(enums.AppStates.START).then(function() {
                            getCsrfAndTriggerLogin();
                        });
                    }

                    construct();
                }
            ])
    });
