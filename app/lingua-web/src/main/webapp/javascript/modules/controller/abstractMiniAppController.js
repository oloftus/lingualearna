(function() {

    var imports = [ "linguaApp" ];

    define(imports, function(linguaApp) {

        var linguaReaderName = "lingua-reader";
        var dialogName = "lingua-main-dialog";
        var overlayName = "lingua-overlay";
        var closeButtonName = "lingua-dialog-close-main";

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

                commsPipe.send(Components.READER, Components.ANY, Signals.CURRENT_NOTEBOOK_CHANGED);

            }, function() {
                messageHandler.addFreshPageMessage($scope, LocalStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            });
        };
        
        var subscribeToNoteSubmissions = function(commsPipe, $scope, notebookService, messageHandler) {

            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, function() {
                setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            }, Signals.NoteSubmittedSuccess);
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

        var setupDialogs = function($scope) {

            makeDialogsDraggable();
            enableDialogToggle($scope);
        };

        var setupGlobalScope = function($scope, $state) {
            
            $scope.global = {};
            $scope.global.model = {};
            $scope.global.func = {};
            
            $scope.global.model.pageMessages = [];
            $scope.global.properties = Properties;
        };
        
        var setMainState = function(stateName) {
            
            AppStates.MAIN = stateName;
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

        var AbstractRootController = {
            setupSpecialDialogs : setupSpecialDialogs,
            setupDialogs : setupDialogs,
            setupPageMessages : setupPageMessages,
            setupGlobalScope : setupGlobalScope,
            setMainState : setMainState,
            setupNotebookEnvironment : setupNotebookEnvironment, 
            subscribeToNoteSubmissions : subscribeToNoteSubmissions
        };
        
        return AbstractRootController;
    });
})();
