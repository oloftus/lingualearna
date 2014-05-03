(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector",
            "util/ngRegistrationHelper", "underscore", "util/commsPipe" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, ngRegistrationHelper, _) {

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var message = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang, selected);

                $state.go(AppStates.TRANSLATE).then(function() {
                    commsPipe.send(Components.READER, Components.TRANSLATE, message);
                });

                textSelector.clearSelected();
            }
        };

        var ReaderController = function($scope, commsPipe, $state) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });

            $scope.model.sourceLang = "en";
            $scope.model.targetLang = "de";
            $scope.model.currentNotebook = {};
            $scope.model.currentNotebook.url = "url";
            $scope.model.currentNotebook.name = "name";
        };

        ngRegistrationHelper(linguaApp).registerController("readerController",
                [ "$scope", "commsPipe", "$state", ReaderController ]);
    });
})();
