define([ "angular", "underscore", "properties", "commonTypes" ], function() {

	angular.module('linguaApp', [])

	.value("translateServiceUrl", properties.translateServiceUrl).value(
			"languageNamesServiceUrl", properties.languageNamesServiceUrl)

	.factory(
			"translateService",
			[ "$http", "translateServiceUrl",
					function($http, translateServiceUrl) {

						var translate = function(translationRequest, callback) {

							$http({
								method : "POST",
								url : translateServiceUrl,
								data : JSON.stringify(translationRequest),
								headers : {
									'Content-Type' : 'application/json'
								}
							}).success(function(data, status, headers, config) {
								callback(data);
							}).error(function(data, status, headers, config) {
								console.log("error");
							});
						};

						return {
							translate : translate
						};
					} ])

	.factory(
			"languageNamesService",
			[ "$http", "languageNamesServiceUrl",
					function($http, languageNamesServiceUrl) {
				
						var lookup = function(languageNameRequest, callback) {

							$http({
								method : "POST",
								url : languageNamesServiceUrl,
								data : JSON.stringify(languageNameRequest),
								headers : {
									'Content-Type' : 'application/json'
								}
							}).success(function(data, status, headers, config) {
								callback(data);
							}).error(function(data, status, headers, config) {
								console.log("error");
							});
						};

						return {
							lookup : lookup
						};
					} ])

	.controller(
			"translateController",
			[
					"$scope",
					"translateService",
					"languageNamesService",
					function($scope, translateService, languageNamesService) {

						$scope.doTranslate = doTranslate = function() {

							var translationRequest = new TranslationRequest(
									$scope.sourceLang, $scope.targetLang,
									$scope.query);
							translateService.translate(translationRequest,
									function(data) {
										$scope.translations = {
											google : data.translations.Google
										};
									});
						};
						
						var sourceLang, targetLang;
						
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
