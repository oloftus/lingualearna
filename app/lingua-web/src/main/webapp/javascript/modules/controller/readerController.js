(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "underscore", "util/interAppMailbox",
            "util/commonTypes" ];

    define(dependencies, function(linguaApp, abstractController, _) {

        var getSelected = function() {

            var selected = "";
            if (window.getSelection) {
                selected = window.getSelection();
            }
            else if (document.getSelection) {
                selected = document.getSelection();
            }
            else if (document.selection) {
                selected = document.selection.createRange().text;
            }
            return selected;
        };

        var clearSelected = function() {

            if (window.getSelection().empty) {
                window.getSelection().empty();
            }
            else if (window.getSelection().removeAllRanges) {
                window.getSelection().removeAllRanges();
            }
            else if (document.selection) {
                document.selection.empty();
            }
        };

        var mouseupHandler = function(interAppMailbox, $state, $scope) {

            var selected = getSelected().toString();
            if (selected != "") {
                var message = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang, selected);
                $state.go(AppStates.TRANSLATE)
                .then(function() {
                    interAppMailbox.send("reader", "translate", message);
                    clearSelected();
                });
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
