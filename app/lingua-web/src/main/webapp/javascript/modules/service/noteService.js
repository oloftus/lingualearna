App.Service.createNew(function() {

    this.moduleIsCalled("noteService");

    this.imports("linguaApp");
    this.imports("service/crudService");
    this.imports("util/ngRegistrationHelper");
    
    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("noteServiceUrl");

    this.hasDefinition(function(linguaApp, CrudService, ngRegistrationHelper) {

        return CrudService;
    });
});
