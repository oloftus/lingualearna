(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var DIALOG_NAME = "#lingua-main-dialog";

        var PageController = function($scope, $state) {

            $scope.Properties = Properties;

            $(DIALOG_NAME).draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll : false
            });

            $scope.$on("$viewContentLoaded", function() {

                var leftOffset = (($(window).width() - $(DIALOG_NAME).outerWidth()) / 2) + $(window).scrollLeft();

                $(DIALOG_NAME + " .lingua-dialog-view:empty").parent().hide();
                $(DIALOG_NAME + " .lingua-dialog-view:not(:empty)").parent().show();
                $(DIALOG_NAME).css({
                    "left" : leftOffset + "px",
                    "top" : "70px"
                });
            });

            $state.go(AppStates.MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController("pageController", [ "$scope", "$state", PageController ]);
    });
})();
