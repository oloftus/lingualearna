(function() {

    var dependencies = [ "linguaApp", "ui.router", "jquery.ui" ];

    define(dependencies, function(linguaApp) {

        linguaApp.controller("pagesController", [ "$scope", "$state", function($scope, $state){

            $(".lingua-draggable-dialog").draggable({
                handle : ".lingua-dialog-header"
            });
            
            $scope.$on("$viewContentLoaded", function(blah) {
                console.log(blah);
            });

            $state.go(AppStates.MAIN);
        } ]);
    });
})();
