App.Service.createNew(function() {

    this.isCalled("notebookService");

    this.imports("service/crudService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("notesPagesServiceUrl");
    this.usesConstant("notebookServiceUrl");
    this.usesConstant("pageServiceUrl");

    this.hasDefinition(function(CrudService) {

        return function(jsonWebService, notesPagesServiceUrl, notebookServiceUrl, pageServiceUrl) {

            var notebookCrudService = new CrudService(jsonWebService, notebookServiceUrl);
            var pageCrudService = new CrudService(jsonWebService, pageServiceUrl);
            
            var getNotebooksAndPages = function(successCallback, failureCallback) {

                jsonWebService.execute(notesPagesServiceUrl, HttpMethod.GET, null, successCallback, failureCallback,
                        true);
            };

            return {
                getNotebooksAndPages : getNotebooksAndPages,
                create : notebookCrudService.create,
                retrieve : notebookCrudService.retrieve,
                update : notebookCrudService.update,
                remove : notebookCrudService.remove,
                createPage : pageCrudService.create,
                retrievePage : pageCrudService.retrieve,
                updatePage : pageCrudService.update,
                removePage : pageCrudService.remove
            };
        };
    });
});
