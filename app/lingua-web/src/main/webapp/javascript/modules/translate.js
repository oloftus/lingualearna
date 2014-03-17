define([ "angular", "appRoot", "properties", "commonTypes", "util" ], function() {

    linguaApp

    .value("translateServiceUrl", properties.translateServiceUrl).value("languageNamesServiceUrl",
            properties.languageNamesServiceUrl)

    .factory(
            "translateService",
            [
                    "jsonWebService",
                    "translateServiceUrl",
                    function(jsonWebService, translateServiceUrl) {

                        return {
                            translate : function(translationRequest, successCallback, failureCallback) {
                                jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest,
                                        successCallback, failureCallback)
                            }
                        };
                    } ])

    .factory(
            "languageNamesService",
            [
                    "jsonWebService",
                    "languageNamesServiceUrl",
                    function(jsonWebService, languageNamesServiceUrl) {

                        return {
                            lookup : function(languageNameRequest, successCallback, failureCallback) {
                                jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest,
                                        successCallback, failureCallback)
                            }
                        };
                    } ])

    .controller(
            "translateController",
            [
                    "$scope",
                    "translateService",
                    "languageNamesService",
                    function($scope, translateService, languageNamesService) {

                        var sourceLang, targetLang, doTranslate;

                        $scope.doTranslate = doTranslate = function() {

                            var translationRequest = new TranslationRequest($scope.sourceLang, $scope.targetLang,
                                    $scope.query);
                            translateService.translate(translationRequest, function(data) {
                                $scope.translations = {
                                    google : data.translations.Google
                                };
                            });
                        };

                        $scope.sourceLang = sourceLang = initParams.sourceLang;
                        $scope.targetLang = targetLang = initParams.targetLang;
                        $scope.query = initParams.query;

                        languageNamesService.lookup(new LanguageNameRequest(sourceLang), function(data) {
                            $scope.sourceLangName = data.langName;
                        });

                        languageNamesService.lookup(new LanguageNameRequest(targetLang), function(data) {
                            $scope.targetLangName = data.langName;
                        });

                        doTranslate();
                    } ]);
});
