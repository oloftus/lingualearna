App.Service.createNew(function() {

    this.isCalled("noteService");

    this.imports("service/crudService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("noteServiceUrl");
    this.usesConstant("notesByPageServiceUrl");

    this.hasDefinition(function(CrudService) {

        return function(jsonWebService, noteServiceUrl, notesByPageServiceUrl) {
            
            var crudService = new CrudService(jsonWebService, noteServiceUrl);
            
            var getNotesByPage = function(pageId, successCallback, failureCallback) {
                
                var serviceUrlWithParams = notesByPageServiceUrl.replace("{pageId}", pageId);
                jsonWebService.execute(serviceUrlWithParams, HttpMethod.GET, null, successCallback, failureCallback,
                        true);
            };
            
            return {
                getNotesByPage : getNotesByPage,
                create : crudService.create,
                retrieve : crudService.retrieve,
                update : crudService.update,
                remove : crudService.remove
            };
        };
    });
});
