App.Service.createNew(function() {

    this.isCalled("notebookService");

    this.imports("service/crudService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("notesPagesServiceUrl");
    this.usesConstant("notebookServiceUrl");

    this.hasDefinition(function(CrudService) {

        return function(jsonWebService, notesPagesServiceUrl, notebookServiceUrl) {

            var crudService = new CrudService(jsonWebService, notebookServiceUrl);
            
            var getNotebooksAndPages = function(successCallback, failureCallback) {

                jsonWebService.execute(notesPagesServiceUrl, HttpMethod.GET, null, successCallback, failureCallback,
                        true);
            };

            return {
                getNotebooksAndPages : getNotebooksAndPages,
                create : crudService.create,
                retrieve : crudService.retrieve,
                update : crudService.update,
                remove : crudService.remove
            };
        };
    });
});
