define([ "util/commonTypes" ], function() {
    
    translateController = function($scope, translateService, languageNamesService) {

        $scope.doTranslate = function() {

            var translationRequest = new TranslationRequest($scope.sourceLang, $scope.targetLang,
                    $scope.query);
            translateService.translate(translationRequest, function(data) {
                $scope.translations = {
                    google : data.translations.Google
                };
            });
        };

        $scope.sourceLang = initParams.sourceLang;
        $scope.targetLang = initParams.targetLang;
        $scope.query = initParams.query;

        languageNamesService.lookup(new LanguageNameRequest($scope.sourceLang), function(data) {
            $scope.sourceLangName = data.langName;
        });

        languageNamesService.lookup(new LanguageNameRequest($scope.targetLang), function(data) {
            $scope.targetLangName = data.langName;
        });

        $scope.doTranslate();
    };
});
