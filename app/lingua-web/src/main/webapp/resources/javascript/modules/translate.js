define([ "angular", "underscore", "properties", "commonTypes" ], function() {

	angular.module('linguaApp', [])

	.value("translateServiceUrl", properties.translateServiceUrl)

	.factory(
			"translateService",
			[ "$http", "translateServiceUrl",
					function($http, translateServiceUrl) {

						var translate = function(translationRequest, callback) {
							
							$http({
								method : "POST",
								url : translateServiceUrl,
								data: JSON.stringify(translationRequest),
								headers: {'Content-Type': 'application/json'}
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

	.controller("translateController",
			[ "$scope", "translateService", function($scope, translateService) {

				$scope.sourceLang = initParams.sourceLang;
				$scope.targetLang = initParams.targetLang;
				$scope.query = initParams.query;
				
				$scope.doTranslate = function() {
					
					var translationRequest = new TranslationRequest($scope.sourceLang, $scope.targetLang, $scope.query);
					translateService.translate(translationRequest, function(data) {
						$scope.translations = {
								google: data.translations.Google
						};
					});
				};
			} ]);
});
