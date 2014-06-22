App.Service.createNew(function() {

    this.isCalled("noteService");

    this.imports("service/crudService");
    
    this.loads("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("noteServiceUrl");

    this.hasDefinition(function(CrudService) {

        return CrudService;
    });
});
