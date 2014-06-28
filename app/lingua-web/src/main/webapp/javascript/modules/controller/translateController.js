App.Controller.createNew(function() {

    this.isCalled("translateController");

    this.imports("controller/abstractController");
    this.imports("util/appStates");
    
    this.injects("$scope");
    this.injects("$rootScope");
    this.injects("$location");
    this.injects("$state");
    this.injects("service/translateService");
    this.injects("service/languageService");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController, appStates) {

        var populateModelFromTranslationRequest = function($scope, translationRequest) {

            $scope.model.sourceLang = translationRequest.sourceLang;
            $scope.model.targetLang = translationRequest.targetLang;
            $scope.model.query = translationRequest.query;

            $scope.model.includedInTest = true;
        };

        var setLanguagesTitles = function($scope, languageService) {

            languageService.lookupLangName($scope.model.sourceLang, function(langName) {
                $scope.model.sourceLangName = langName;
            });

            languageService.lookupLangName($scope.model.targetLang, function(langName) {
                $scope.model.targetLangName = langName;
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

                appStates.goRelative($state, AppStates.ADD_NOTE, AppStates.READER_MAIN).then(function() {
                    commsPipe.send(Components.TRANSLATE, Components.ADD_NOTE, Subjects.NOTE, message);
                });
            };
        };

        var subscribeToTranslationRequests = function(commsPipe, $scope, languageService) {

            var subscriberId = commsPipe.subscribe(Components.ANY, Components.TRANSLATE, function(translationRequest, subject) {
                populateModelFromTranslationRequest($scope, translationRequest);
                setLanguagesTitles($scope, languageService);
            }, Subjects.TRANSLATION_REQUEST);
            
            abstractController.addCleanupStep($scope, function() {
                commsPipe.unsubscribe(subscriberId);
            });
        };

        return function($scope, $rootScope, $location, $state, translateService, languageService, commsPipe) {

            abstractController.setupDefaultScope($scope);
            abstractController.setupCleanup($scope, $rootScope);
            addTranslateButtonHandler($scope, translateService);
            addAddToNotebookButtonHandler($scope, commsPipe, $state, $location);
            subscribeToTranslationRequests(commsPipe, $scope, languageService);
        };
    });
});
