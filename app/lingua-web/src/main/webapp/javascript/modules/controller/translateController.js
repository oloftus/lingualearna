App.Controller.createNew(function() {

    this.isCalled("translateController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.loads("util/commsPipe");
    this.loads("service/languageNamesService");
    this.loads("service/translateService");

    this.dependsOnNg("$scope");
    this.dependsOnNg("translateService");
    this.dependsOnNg("languageNamesService");
    this.dependsOnNg("commsPipe");
    this.dependsOnNg("$location");
    this.dependsOnNg("$state");

    this.hasDefinition(function(abstractController, _) {

        var populateModelFromTranslationRequest = function($scope, translationRequest) {

            $scope.model.sourceLang = translationRequest.sourceLang;
            $scope.model.targetLang = translationRequest.targetLang;
            $scope.model.query = translationRequest.query;

            $scope.model.includedInTest = true;
        };

        var setLanguagesTitles = function($scope, languageNamesService) {

            languageNamesService.lookup(new LanguageNameRequest($scope.model.sourceLang), function(data) {
                $scope.model.sourceLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.targetLang), function(data) {
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

        var subscribeToTranslationRequests = function(commsPipe, $scope, languageNamesService) {

            commsPipe.subscribe(Components.ANY, Components.TRANSLATE, function(translationRequest, subject) {
                populateModelFromTranslationRequest($scope, translationRequest);
                setLanguagesTitles($scope, languageNamesService);
            }, Subjects.TRANSLATION_REQUEST);
        };

        return function($scope, translateService, languageNamesService, commsPipe, $location, $state) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            addTranslateButtonHandler($scope, translateService);
            addAddToNotebookButtonHandler($scope, commsPipe, $state, $location);
            subscribeToTranslationRequests(commsPipe, $scope, languageNamesService);
        };
    });
});
