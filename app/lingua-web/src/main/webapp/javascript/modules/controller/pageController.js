(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        var DIALOG_NAME = "#lingua-main-dialog";
        
        var PageController = function($scope, $state) {

            $(DIALOG_NAME).draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll : false
            });

            $scope.$on("$viewContentLoaded", function() {
                
                var leftOffset = (($(window).width() - $(DIALOG_NAME).outerWidth()) / 2)
                        + $(window).scrollLeft();
                
                $(DIALOG_NAME + " .lingua-dialog-view:empty").parent().hide();
                $(DIALOG_NAME + " .lingua-dialog-view:not(:empty)").parent().show();
                $(DIALOG_NAME).css({
                    "left" : leftOffset + "px",
                    "top" : "50px"
                });
            });

            $state.go(AppStates.MAIN);
        };

        linguaApp.controller("pageController", [ "$scope", "$state", PageController ]);
    });
})();
