App.Service.createNew(function() {

    this.moduleIsCalled("notebookService");

    this.imports("linguaApp");
    this.imports("util/ngRegistrationHelper");

    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("notesPagesServiceUrl");

    this.hasDefinition(function(linguaApp, ngRegistrationHelper) {

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
