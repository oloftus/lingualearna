(function() {

    var dependencies = [ "linguaApp", "ui.router" ];

    define(dependencies, function(linguaApp) {

        linguaApp.controller("pagesController", [ "$state", function($state){
            
            $state.go(AppStates.MAIN);
        } ]);
    });
})();
