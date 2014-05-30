(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

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
