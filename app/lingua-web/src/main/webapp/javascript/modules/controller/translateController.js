(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "util/commsPipe", "service/languageNamesService", "service/translateService" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var populateModel = function($scope, translationRequest) {

            $scope.model.sourceLang = translationRequest.sourceLang;
            $scope.model.targetLang = translationRequest.targetLang;
            $scope.model.query = translationRequest.query;
        };

        var initDialog = function($scope, languageNamesService) {

            languageNamesService.lookup(new LanguageNameRequest($scope.model.sourceLang), function(data) {
                $scope.model.sourceLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.targetLang), function(data) {
                $scope.model.targetLangName = data.langName;
            });

            $scope.func.doTranslate();
        };

        var TranslateController = function($scope, translateService, languageNamesService, commsPipe, $location,
                $state) {

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

            $scope.func.doAddToNotebook = function() {

                var message = new Note($scope.model.targetLang, $scope.model.translations.google,
                        $scope.model.sourceLang, $scope.model.query, "", $location.absUrl(), TranslationSources.GOOGLE);

                $state.go(AppStates.ADD_NOTE).then(function() {
                    commsPipe.send(Components.TRANSLATE, Components.ADD_NOTE, message);
                });
            };

            commsPipe.subscribe(Components.ANY, Components.TRANSLATE, function(translationRequest) {

                populateModel($scope, translationRequest);
                initDialog($scope, languageNamesService);
            });
        };

        ngRegistrationHelper(linguaApp).registerController(
                "translateController",
                [ "$scope", "translateService", "languageNamesService", "commsPipe", "$location", "$state",
                        TranslateController ]);
    });
})();
