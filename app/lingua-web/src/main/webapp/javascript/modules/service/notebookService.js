App.Service.createNew(function() {

    this.isCalled("notebookService");

    this.imports("rootApp");

    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("notesPagesServiceUrl");

    this.hasDefinition(function(rootApp) {

        return function(jsonWebService, notesPagesServiceUrl) {

            var getNotebooksAndPages = function(successCallback, failureCallback) {

                jsonWebService.execute(notesPagesServiceUrl, HttpMethod.GET, null, successCallback, failureCallback,
                        true);
            };

            return {
                getNotebooksAndPages : getNotebooksAndPages
            };
        };
    });
});
