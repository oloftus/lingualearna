define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    var translateController = function($scope, translateService, languageNamesService, sourceLang, targetLang,
            initQuery) {

        $scope.model = {};
        $scope.func = {};
        
        $scope.model.sourceLang = sourceLang;
        $scope.model.targetLang = targetLang;
        $scope.model.query = initQuery;

        $scope.func.doTranslate = function() {

            var translationRequest = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang, $scope.model.query);
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

    linguaApp.controller("translateController", [ "$scope", "translateService", "languageNamesService", "sourceLang",
            "targetLang", "initQuery", translateController ]);
});
