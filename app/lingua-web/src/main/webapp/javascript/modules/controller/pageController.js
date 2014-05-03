(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService", "util/commsPipe" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";
        var linguaReaderName = "lingua-reader";
        var dialogName = "lingua-main-dialog";
        var overlayName = "lingua-overlay";
        var closeButtonName = "lingua-dialog-close-main";

        var manageLoginOverlay = function($scope) {

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

        var doLoginAndCsrf = function(jsonWebService, $state, commsPipe, $scope) {

            manageLoginOverlay($scope);

            jsonWebService.executeSimple(Properties.csrfTokenApiUrl, function(data, status) {

                linguaApp.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = data;
                $state.go(AppStates.MAIN);
            }, function(data, status) {

                if (status === HttpHeaders.PSEUDO_CSRF_NOT_PERMITTED) {
                    commsPipe.subscribe(Components.LOGIN, Components.PAGE, function(message) {
                        doLoginAndCsrf(jsonWebService, $state, commsPipe, $scope);
                    });
                    $state.go(AppStates.LOGIN);
                }
            });
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

        var PageController = function($scope, $state, jsonWebService, commsPipe) {

            $scope.Properties = Properties;
            setupDialogs($scope);
            doLoginAndCsrf(jsonWebService, $state, commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("pageController",
                [ "$scope", "$state", "jsonWebService", "commsPipe", PageController ]);
    });
})();
;