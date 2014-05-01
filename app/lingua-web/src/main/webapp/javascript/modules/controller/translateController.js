(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "underscore", "util/interAppMailbox",
            "service/languageNamesService", "service/translateService", "util/commonTypes", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, _) {

        var populateModel = function($scope, translationRequest) {

            $scope.model.sourceLang = translationRequest.sourceLang;
            $scope.model.targetLang = translationRequest.targetLang;
            $scope.model.query = translationRequest.query;
        };

        var doTranslation = function($scope, languageNamesService) {

            languageNamesService.lookup(new LanguageNameRequest($scope.model.sourceLang), function(data) {
                $scope.model.sourceLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.targetLang), function(data) {
                $scope.model.targetLangName = data.langName;
            });

            $scope.func.doTranslate();
        };

        var TranslateController = function($scope, translateService, languageNamesService, interAppMailbox, $state,
                $rootScope) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $scope.func.doTranslate = function() {

                var translationRequest = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang,
                        $scope.model.query);
                translateService.translate(translationRequest, function(data) {
                    $scope.model.translations = {
                        google : data.translations.Google
                    };
                });
            };

            interAppMailbox.subscribe("reader", "translate", function(messages) {

                var translationRequest = messages[messages.length - 1];
                populateModel($scope, translationRequest);
                doTranslation($scope, languageNamesService);
            });
        };

        linguaApp.controllerProvider.register("translateController", [ "$scope", "translateService",
                "languageNamesService", "interAppMailbox", "$state", "$rootScope", TranslateController ]);
    });
})();
