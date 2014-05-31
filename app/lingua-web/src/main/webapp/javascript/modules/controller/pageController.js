(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "util/textSelector", "service/jsonWebService", "util/messageHandler",
             "util/commsPipe", "service/notebookService"];

    define(dependencies, function(linguaApp, ngRegistrationHelper, textSelector) {

        var linguaReaderName = "lingua-reader";
        var dialogName = "lingua-main-dialog";
        var overlayName = "lingua-overlay";
        var closeButtonName = "lingua-dialog-close-main";

        var setupSpecialDialogs = function($scope) {

            $scope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {

                $overlay = $("#" + overlayName);
                $closeButton = $("#" + closeButtonName);
                $linguaReader = $("#" + linguaReaderName);
                var $dialog = $("#" + dialogName);

                if (toState.name === AppStates.LOGIN) {
                    
                    if ($overlay.length == 0) {
                        $dialog.before("<div id='" + overlayName + "'></div>");
                    }
                    $closeButton.hide();
                    $linguaReader.css({
                        "height" : "100%"
                    });
                }
                else {
                    
                    $overlay.remove();
                    $closeButton.show();
                    $linguaReader.css({
                        "height" : "auto"
                    });
                }
            });
        };

        var triggerLogin = function(jsonWebService) {

            jsonWebService.getCsrfToken();
        };
        
        var makeDialogsDraggable = function() {
            
            var $dialog = $("#" + dialogName);

            $dialog.draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll : false
            });
        };
        
        var enableDialogToggle = function($scope) {
            
            var $dialog = $("#" + dialogName);

            $scope.$on("$viewContentLoaded", function() {

                var $window = $(window);
                var leftOffset = (($window.width() - $dialog.outerWidth()) / 2) + $window.scrollLeft();

                $dialog.find(".lingua-dialog-view:empty").parent().hide();
                $dialog.find(".lingua-dialog-view:not(:empty)").parent().show();
                $dialog.css({
                    "left" : leftOffset + "px",
                    "top" : "70px"
                });
            });
        };

        var setupDialogs = function($scope) {

            makeDialogsDraggable();
            enableDialogToggle($scope);
        };
        
        var setupGlobalScope = function($scope) {
            
            $scope.global = {};
            $scope.global.model = {};
            $scope.global.func = {};
            
            $scope.global.model.pageMessages = [];
            $scope.global.properties = Properties;
        };

        var setupPageMessages = function($scope, messageHandler, $timeout) {
            
            var pageMessageTimeout = null;
            $scope.$watchCollection("global.model.pageMessages", function(newMessages, oldMessages) {
                
                if (newMessages.length > oldMessages.length) {
                    if (!_.isNull(pageMessageTimeout)) {
                        $timeout.cancel(pageMessageTimeout);
                    }
                    pageMessageTimeout = $timeout(function() {
                        messageHandler.clearPageMessages($scope);
                    }, Properties.pageMessagesTimeout);
                }
            });
        };

        var setupNotebookEnvironment = function($scope, notebookService, commsPipe, messageHandler) {

            notebookService.getNotebooksAndPages(function(notebooks) {

                $scope.global.model.notebooks = notebooks;

                var done = false;
                _.some(notebooks, function(notebook) {
                    _.some(notebook.pages, function(page) {

                        if (page.lastUsed) {
                            $scope.global.model.currentNotebook = notebook;
                            $scope.global.model.currentPage = page;
                            done = true;
                        }

                        return done;
                    });
                    return done;
                });

                commsPipe.send(Components.READER, Components.ANY, Signals.CurrentNotebookChanged);

            }, function() {
                messageHandler.addFreshPageMessage($scope, LocalStrings.genericServerErrorMessage, MessageSeverity.ERROR);
            });
        };

        var subscribeToNoteSubmissions = function(commsPipe, $scope, notebookService, messageHandler) {

            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, function(message) {
                if (message === Signals.NoteSubmittedSuccess) {
                    setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
                }
            });
        };

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var translationRequest = new TranslationRequest($scope.global.model.currentNotebook.localLang,
                        $scope.global.model.currentNotebook.foreignLang, selected);

                $state.go(AppStates.TRANSLATE).then(
                        function() {
                            commsPipe.send(Components.READER, Components.TRANSLATE, translationRequest,
                                    Subjects.TranslationRequest);
                        });

                textSelector.clearSelected();
            }
        };

        var setupClickToTranslate = function(commsPipe, $state, $scope) {

            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });
        };

        var PageController = function($scope, $state, jsonWebService, $timeout, messageHandler, notebookService, commsPipe) {

            setupGlobalScope($scope);
            setupPageMessages($scope, messageHandler, $timeout);
            setupDialogs($scope);
            setupSpecialDialogs($scope);
            triggerLogin(jsonWebService, $scope);
            
            setupClickToTranslate(commsPipe, $state, $scope);
            setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
            
            $state.go(AppStates.MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController("pageController",
                [ "$scope", "$state", "jsonWebService", "$timeout", "messageHandler", "notebookService", "commsPipe", PageController ]);
    });
})();
