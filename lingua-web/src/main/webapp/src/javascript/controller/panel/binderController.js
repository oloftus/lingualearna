define(
    [
        "module/ngModule",
        "types/enums",
        "service/noteService",
        "service/languageService",
        "ui/notificationManager",
        "utility/messageBus",
        "controller/abstractController",
        "directive/lgCurrentNotebookSelector"
    ],
    function (ngModule, enums) {

        ngModule.controller("binderController",
            [
                "$scope",
                "noteService",
                "languageService",
                "notificationManager",
                "messageBus",
                "abstractController",

                function ($scope, noteService, languageService, NotificationHandler, messageBus, AbstractController) {

                    var notificationManager = new NotificationHandler($scope);
                    var abstractController = new AbstractController($scope);

                    function loadNotesIntoPage() {

                        if ($scope.global.model.currentPage) {
                            noteService.getNotesByPage($scope.global.model.currentPage.pageId, function (notes) {
                                $scope.global.model.currentPage.notes = notes;
                            }, function (data, status, headers) {
                                notificationManager.handleErrors(data, status, headers);
                            });
                        }
                    }

                    function setupNotesReloadConditions() {

                        function reloadNotes () {
                            loadNotesIntoPage();
                        }

                        messageBus.subscribe(enums.Components.BINDER, enums.Components.ANY, reloadNotes, enums.Signals.CURRENT_PAGE_CHANGED);
                        messageBus.send(enums.Components.BINDER, enums.Components.NOTEBOOK, enums.Signals.DONE_SETUP);
                    }

                    function setupNotebookLanguageTitlesListener() {

                        function notebookLanguageTitlesListener () {

                            languageService.lookupLangName($scope.global.model.currentNotebook.foreignLang, function (langName) {
                                $scope.global.model.currentNotebook.foreignLangName = langName;
                            });

                            languageService.lookupLangName($scope.global.model.currentNotebook.localLang, function (langName) {
                                $scope.global.model.currentNotebook.localLangName = langName;
                            });
                        }
                        messageBus.subscribe(enums.Components.ANY, enums.Components.ANY, notebookLanguageTitlesListener,
                            enums.Signals.CURRENT_NOTEBOOK_CHANGED);
                    }

                    function construct() {

                        abstractController.setup();
                        setupNotesReloadConditions();
                        setupNotebookLanguageTitlesListener();
                    }

                    construct();
                }
            ]);
    });
