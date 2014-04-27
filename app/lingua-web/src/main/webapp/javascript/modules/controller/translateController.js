(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "underscore", "service/languageNamesService",
            "service/translateService", "util/commonTypes", "underscore" ];

    define(dependencies, function(linguaApp, abstractController, _) {

        var TranslateController = function($scope, translateService, languageNamesService, sourceLang, targetLang,
                initQuery) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $scope.model.sourceLang = sourceLang;
            $scope.model.targetLang = targetLang;
            $scope.model.query = initQuery;

            $scope.func.doTranslate = function() {

                var translationRequest = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang,
                        $scope.model.query);
                translateService.translate(translationRequest, function(data) {
                    $scope.model.translations = {
                        google : data.translations.Google
                    };
                });
            };

            languageNamesService.lookup(new LanguageNameRequest($scope.model.sourceLang), function(data) {
                $scope.model.sourceLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.targetLang), function(data) {
                $scope.model.targetLangName = data.langName;
            });

            $scope.func.doTranslate();
        };

        // Temporary
        linguaApp.provide.constant("sourceLang", "en");
        linguaApp.provide.constant("targetLang", "de");
        linguaApp.provide.constant("initQuery", "");
        linguaApp.controllerProvider.register("translateController", [ "$scope", "translateService",
                "languageNamesService", "sourceLang", "targetLang", "initQuery", TranslateController ]);
    });
})();
