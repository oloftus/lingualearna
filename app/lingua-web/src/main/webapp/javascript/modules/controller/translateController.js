App.Controller.createNew(function() {

    this.isCalled("translateController");

    this.injects("$scope");
    this.injects("$location");
    this.injects("$state");
    this.injects("service/translateService");
    this.injects("service/languageService");
    this.injects("util/commsPipe");

    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        var populateModelFromTranslationRequest = function($scope, translationRequest) {

            $scope.model.sourceLang = translationRequest.sourceLang;
            $scope.model.targetLang = translationRequest.targetLang;
            $scope.model.query = translationRequest.query;

            $scope.model.includedInTest = true;
        };

        var setLanguagesTitles = function($scope, languageService) {

            languageService.lookupLangName(new LanguageNameRequest($scope.model.sourceLang), function(data) {
                $scope.model.sourceLangName = data.langName;
            });

            languageService.lookupLangName(new LanguageNameRequest($scope.model.targetLang), function(data) {
                $scope.model.targetLangName = data.langName;
            });

            $scope.func.doTranslate();
        };

        var addTranslateButtonHandler = function($scope, translateService) {

            $scope.func.doTranslate = function() {

                var translationRequest = new TranslationRequest($scope.model.sourceLang, $scope.model.targetLang,
                        $scope.model.query);

                translateService.translate(translationRequest, function(data) {
                    $scope.model.translations = {
                        google : data.translations.Google
                    };
                });
            };
        };

        var addAddToNotebookButtonHandler = function($scope, commsPipe, $state, $location) {

            $scope.func.doAddToNotebook = function() {

                var additionalNotes = "";
                var message = new Note($scope.model.targetLang, $scope.model.translations.google,
                        $scope.model.sourceLang, $scope.model.query, additionalNotes, $location.absUrl(),
                        TranslationSources.GOOGLE, $scope.model.includedInTest);

                $state.go(AppStates.ADD_NOTE).then(function() {
                    commsPipe.send(Components.TRANSLATE, Components.ADD_NOTE, Subjects.NOTE, message);
                });
            };
        };

        var subscribeToTranslationRequests = function(commsPipe, $scope, languageService) {

            var subscriberId = commsPipe.subscribe(Components.ANY, Components.TRANSLATE, function(translationRequest, subject) {
                populateModelFromTranslationRequest($scope, translationRequest);
                setLanguagesTitles($scope, languageService);
            }, Subjects.TRANSLATION_REQUEST);
            
            return subscriberId;
        };

        return function($scope, $location, $state, translateService, languageService, commsPipe) {

            this.setupDefaultScope($scope);
            addTranslateButtonHandler($scope, translateService);
            addAddToNotebookButtonHandler($scope, commsPipe, $state, $location);
            
            var subscriberId = subscribeToTranslationRequests(commsPipe, $scope, languageService);
            this.addCleanupStep($scope, function() {
                commsPipe.unsubscribe(subscriberId);
            });
        };
    });
});
