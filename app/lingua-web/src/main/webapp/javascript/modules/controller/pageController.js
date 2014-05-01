(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        var PageController = function($scope, $state) {

            $(".lingua-draggable-dialog").draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll : false
            });

            $scope.$on("$viewContentLoaded", function() {
                $(".lingua-draggable-dialog .lingua-dialog-view:empty").parent().hide();
                $(".lingua-draggable-dialog .lingua-dialog-view:not(:empty)").parent().show();
            });

            $state.go(AppStates.MAIN);
        };

        linguaApp.controller("pageController", [ "$scope", "$state", PageController ]);
    });
})();
