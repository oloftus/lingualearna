(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService", "util/commsPipe" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        var doLoginAndCsrf = function(jsonWebService, $state, commsPipe) {

            jsonWebService.executeSimple(Properties.csrfTokenApiUrl, function(data, status) {
                
                linguaApp.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = data;
                $state.go(AppStates.MAIN);
            }, function(data, status) {

                if (status === HttpHeaders.PSEUDO_CSRF_NOT_PERMITTED) {
                    commsPipe.subscribe(Components.LOGIN, Components.PAGE, function(message) {
                        doLoginAndCsrf(jsonWebService, $state, commsPipe);
                    });
                    $state.go(AppStates.LOGIN);
                }
            });
        };

        var setupDialogs = function($scope) {

            var dialogName = "#lingua-main-dialog";

            $(dialogName).draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll : false
            });

            $scope.$on("$viewContentLoaded", function() {

                var leftOffset = (($(window).width() - $(dialogName).outerWidth()) / 2) + $(window).scrollLeft();

                $(dialogName + " .lingua-dialog-view:empty").parent().hide();
                $(dialogName + " .lingua-dialog-view:not(:empty)").parent().show();
                $(dialogName).css({
                    "left" : leftOffset + "px",
                    "top" : "70px"
                });
            });
        };

        var PageController = function($scope, $state, jsonWebService, commsPipe) {

            $scope.Properties = Properties;
            setupDialogs($scope);
            doLoginAndCsrf(jsonWebService, $state, commsPipe);
        };

        ngRegistrationHelper(linguaApp).registerController("pageController",
                [ "$scope", "$state", "jsonWebService", "commsPipe", PageController ]);
    });
})();
