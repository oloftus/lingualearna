(function() {

    var imports = [];
    
    imports.push("linguaApp");

    define(doImport(imports), function(linguaApp) {

        var DIALOG_TOP_OFFSET = 70;
        
        var READER_NAME = "lingua-reader";
        var DIALOG_NAME = "lingua-main-dialog";
        var DIALOG_HEADER_NAME = "lingua-dialog-header";
        var DIALOG_VIEW_NAME = "lingua-dialog-view";
        var OVERLAY_NAME = "lingua-overlay";
        var CLOSE_BUTTON_NAME = "lingua-dialog-close-main";

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
        
        var asId = function(idName) {
            
            return "#" + idName;
        };
        
        var asClass = function(className) {
            
            return "." + className;
        };
        
        var makeDialogsDraggable = function() {
            
            var $dialog = $(asId(DIALOG_NAME));

            $dialog.draggable({
                handle : asClass(DIALOG_HEADER_NAME),
                containment : "document",
                scroll : false
            });
        };
        
        var enableDialogToggle = function($scope) {
            
            var $dialog = $(asId(DIALOG_NAME));

            $scope.$on("$viewContentLoaded", function() {

                var $window = $(window);
                var leftOffset = (($window.width() - $dialog.outerWidth()) / 2) + $window.scrollLeft();

                $dialog.find(asClass(DIALOG_VIEW_NAME) + ":empty").parent().hide();
                $dialog.find(asClass(DIALOG_VIEW_NAME) + ":not(:empty)").parent().show();
                $dialog.css({
                    "left" : leftOffset + "px",
                    "top" : DIALOG_TOP_OFFSET + "px"
                });
            });
        };
        
        var setupSpecialDialogs = function($scope) {

            $scope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {

                var $overlay = $(asId(OVERLAY_NAME));
                var $closeButton = $(asId(CLOSE_BUTTON_NAME));
                var $linguaReader = $(asId(READER_NAME));
                var $dialog = $(asId(DIALOG_NAME));

                if (toState.name.endsWith(AppStates.LOGIN)) {
                    
                    if ($overlay.length == 0) {
                        $dialog.before("<div id='" + OVERLAY_NAME + "'></div>");
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

        var AbstractRootController = {
            setupSpecialDialogs : setupSpecialDialogs,
            setupDialogs : setupDialogs,
            setupPageMessages : setupPageMessages,
            setupGlobalScope : setupGlobalScope,
            setupNotebookEnvironment : setupNotebookEnvironment, 
            subscribeToNoteSubmissions : subscribeToNoteSubmissions
        };
        
        return AbstractRootController;
    });
})();
