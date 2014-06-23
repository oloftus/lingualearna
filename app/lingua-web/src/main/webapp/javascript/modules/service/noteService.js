App.Service.createNew(function() {

    this.isCalled("noteService");

    this.imports("service/crudService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("noteServiceUrl");

    this.hasDefinition(function(CrudService) {

        return CrudService;
    });
});
