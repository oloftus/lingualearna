(function() {

    var dependencies = [ "linguaApp", "ui.router", "jquery.ui" ];

    define(dependencies, function(linguaApp) {

        linguaApp.controller("pagesController", [ "$scope", "$state", function($scope, $state) {

            $(".lingua-draggable-dialog").draggable({
                handle : ".lingua-dialog-header",
                containment : "document",
                scroll: false
            });

            $scope.$on("$viewContentLoaded", function(blah) {
                $(".lingua-draggable-dialog .view:empty").parent().hide();
                $(".lingua-draggable-dialog .view:not(:empty)").parent().show();
            });

            $state.go(AppStates.MAIN);
        } ]);
    });
})();
