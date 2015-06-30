define(
    [
        "module/ngModule",
        "types/types",
        "types/enums",
        "utility/cleanupManager",
        "underscore",
        "service/translateService",
        "service/languageService",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"
    ],
    function (ngModule, types, enums, CleanupManager, _) {

        var GOOGLE = "Google";
        var COLLINS = "Collins";
        var ALL = "All";

        var SAVABLE_TRANSLATIONS = [
            {
                name: GOOGLE,
                displayName: "Translation"
            },
            {
                name: COLLINS,
                displayName: "Dictionary definition"
            }
        ];

        var ALL_TRANSLATIONS = {
            name: ALL,
            displayName: "Both"
        };

        ngModule.controller("translateController",
            [
                "$scope",
                "$rootScope",
                "$location",
                "$state",
                "translateService",
                "languageService",
                "messageBus",
                "stateNavigator",
                "abstractController",

                function ($scope, $rootScope, $location, $state, translateService, languageService, messageBus,
                          stateNavigator, AbstractController) {

                    var cleanupManager = new CleanupManager($scope);
                    var abstractController = new AbstractController($scope);

                    function populateModelFromEnvironment() {

                        $scope.model.sourceLang = $scope.global.model.currentNotebook.foreignLang;
                        $scope.model.targetLang = $scope.global.model.currentNotebook.localLang;
                    }

                    function populateModelFromTranslationRequest(translationRequest) {

                        $scope.model.sourceLang = translationRequest.sourceLang;
                        $scope.model.targetLang = translationRequest.targetLang;
                        $scope.model.query = translationRequest.query;
                        $scope.model.sourceUrl = $location.absUrl();
                        $scope.model.sourceContext = translationRequest.sourceContext;
                        $scope.model.dialogWasAutoOpened = true;
                    }

                    function setLanguagesTitles() {

                        languageService.lookupLangName($scope.model.sourceLang, function (langName) {
                            $scope.model.sourceLangName = langName;
                        });

                        languageService.lookupLangName($scope.model.targetLang, function (langName) {
                            $scope.model.targetLangName = langName;
                        });
                    }

                    function savableTranslationIsActive(providerName) {

                        var providerNames = _.pluck($scope.model.savableTranslations, "name");
                        return _.contains(providerNames, providerName);
                    }

                    function getSavableTranslationInfo(saveableTranslationName) {

                        var searchCriteria = {
                            name: saveableTranslationName
                        };

                        var list = _.union(SAVABLE_TRANSLATIONS, ALL_TRANSLATIONS);
                        return _.findWhere(list, searchCriteria);
                    }

                    function setDefaultSavableTranslationIfActive(saveableTranslationName) {

                        if (savableTranslationIsActive(saveableTranslationName)) {
                            $scope.model.notesToSave = getSavableTranslationInfo(saveableTranslationName);
                        }
                    }

                    function allTranslationsActive() {

                        return _.size($scope.model.translations) === SAVABLE_TRANSLATIONS.length;
                    }

                    function populateSavableTranslations() {

                        $scope.model.savableTranslations = [];

                        _.each($scope.model.translations, function (translation, translationName) {

                            if (!_.isEmpty(translation)) {
                                $scope.model.savableTranslations.push(getSavableTranslationInfo(translationName));
                            }
                        });

                        if (allTranslationsActive()) {
                            $scope.model.savableTranslations.push(getSavableTranslationInfo(ALL));
                        }

                        setDefaultSavableTranslationIfActive(GOOGLE);
                        setDefaultSavableTranslationIfActive(COLLINS);
                        setDefaultSavableTranslationIfActive(ALL);
                    }

                    function doTranslate(phrase) {

                        if (phrase) {
                            $scope.model.query = phrase;
                        }

                        var translationRequest = new types.TranslationRequest($scope.model.sourceLang,
                            $scope.model.targetLang, $scope.model.query);

                        translateService.translate(translationRequest, function (data) {

                            $scope.model.didYouMean = {};
                            $scope.model.translations = data.translations;
                            populateSavableTranslations();

                            if (!_.isEmpty(data.providerSpecificResponses.Collins)) {
                                $scope.model.didYouMean.Collins = data.providerSpecificResponses.Collins;
                            }
                        });
                    }

                    function addToNotebook() {

                        var includedInTest = true;
                        var translationSource;
                        var foreignLang = $scope.model.sourceLang;
                        var foreignNote = $scope.model.query;
                        var localLang = $scope.model.targetLang;
                        var localNote = "";
                        var additionalNotes = "";

                        switch ($scope.model.notesToSave) {
                            case getSavableTranslationInfo(GOOGLE):
                                translationSource = enums.TranslationSources.GOOGLE;
                                localNote = $scope.model.translations.Google;
                                break;
                            case getSavableTranslationInfo(COLLINS):
                                translationSource = enums.TranslationSources.COLLINS;
                                additionalNotes = $scope.model.translations.Collins;
                                break;
                            case getSavableTranslationInfo(ALL):
                                translationSource = enums.TranslationSources.COLLINS_GOOGLE;
                                localNote = $scope.model.translations.Google;
                                additionalNotes = $scope.model.translations.Collins;
                                break;
                        }

                        var message = new types.Note(foreignLang, foreignNote,
                            localLang, localNote, additionalNotes, $scope.model.sourceUrl,
                            $scope.model.sourceContext, translationSource, includedInTest);

                        stateNavigator.goToSiblingState(enums.AppStates.ADD_NOTE).then(function () {
                            messageBus.send(enums.Components.TRANSLATE, enums.Components.ADD_NOTE,
                                enums.Subjects.NOTE, message);
                        });
                    }

                    function setupClickHandlers() {

                        $scope.func.doTranslate = function (phrase) {

                            doTranslate(phrase);
                        };

                        $scope.func.doAddToNotebook = function () {

                            addToNotebook();
                        };
                    }

                    function subscribeToTranslationRequests() {

                        var subscriberId = messageBus.subscribe(enums.Components.ANY, enums.Components.TRANSLATE,
                            function (translationRequest) {
                                populateModelFromTranslationRequest(translationRequest);
                                setLanguagesTitles($scope, languageService);
                                doTranslate();
                            }, enums.Subjects.TRANSLATION_REQUEST);

                        cleanupManager.addCleanupStep(function () {
                            messageBus.unsubscribe(subscriberId);
                        });
                    }

                    function construct() {

                        abstractController.setup();
                        setupClickHandlers();
                        subscribeToTranslationRequests();
                        populateModelFromEnvironment();
                        setLanguagesTitles();
                    }

                    construct();
                }
            ]);
    });
