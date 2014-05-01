(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector", "underscore",
            "util/interAppMailbox" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, _) {

        var mouseupHandler = function(interAppMailbox, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var message = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang, selected);
                
                $state.go(AppStates.TRANSLATE).then(function() {
                    interAppMailbox.send(Components.READER, Components.TRANSLATE, message);
                });
                
                textSelector.clearSelected();
            }
        };

        var ReaderController = function($scope, interAppMailbox, $state) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $(document).bind("mouseup", function() {
                mouseupHandler(interAppMailbox, $state, $scope);
            });

            $scope.model.sourceLang = "en";
            $scope.model.targetLang = "de";
            $scope.model.currentNotebook = {};
            $scope.model.currentNotebook.url = "url";
            $scope.model.currentNotebook.name = "name";
        };

        linguaApp.controllerProvider.register("readerController", [ "$scope", "interAppMailbox", "$state",
                ReaderController ]);
    });
})();
