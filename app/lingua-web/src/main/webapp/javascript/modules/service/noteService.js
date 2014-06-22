App.Service.createNew(function() {

    this.isCalled("noteService");

    this.imports("rootApp");
    this.imports("service/crudService");
    
    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("noteServiceUrl");

    this.hasDefinition(function(rootApp, CrudService) {

        return CrudService;
    });
});
