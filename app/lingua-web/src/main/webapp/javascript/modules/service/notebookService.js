(function() {

    var imports = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(imports, function(linguaApp, ngRegistrationHelper) {

        var NotebookService = function(jsonWebService, notesPagesServiceUrl) {

            var getNotebooksAndPages = function(successCallback, failureCallback) {

                jsonWebService.execute(notesPagesServiceUrl, HttpMethod.GET, null, successCallback, failureCallback,
                        true);
            };

            return {
                getNotebooksAndPages : getNotebooksAndPages
            };
        };

        ngRegistrationHelper(linguaApp).registerService("notebookService",
                [ "jsonWebService", "notesPagesServiceUrl", NotebookService ]);
    });
})();
