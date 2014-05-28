(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

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
            
            $scope.global.properties = Properties;
        };

        var PageController = function($scope, $state, jsonWebService) {

            setupGlobalScope($scope);
            setupDialogs($scope);
            setupSpecialDialogs($scope);
            triggerLogin(jsonWebService, $scope);
            $state.go(AppStates.MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController("pageController",
                [ "$scope", "$state", "jsonWebService", PageController ]);
    });
})();
