define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    var translateController = function($scope, translateService, languageNamesService, sourceLang, targetLang,
            initQuery) {

        $scope.sourceLang = sourceLang;
        $scope.targetLang = targetLang;
        $scope.query = initQuery;

        $scope.doTranslate = function() {

            var translationRequest = new TranslationRequest($scope.sourceLang, $scope.targetLang, $scope.query);
            translateService.translate(translationRequest, function(data) {
                $scope.translations = {
                    google : data.translations.Google
                };
            });
        };

        languageNamesService.lookup(new LanguageNameRequest($scope.sourceLang), function(data) {
            $scope.sourceLangName = data.langName;
        });

        languageNamesService.lookup(new LanguageNameRequest($scope.targetLang), function(data) {
            $scope.targetLangName = data.langName;
        });

        $scope.doTranslate();
    };

    linguaApp.controller("translateController", [ "$scope", "translateService", "languageNamesService", "sourceLang",
            "targetLang", "initQuery", translateController ]);
});
