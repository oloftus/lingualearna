App.Service.createNew(function() {

    this.isCalled("notebookService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("notesPagesServiceUrl");

    this.hasDefinition(function() {

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
