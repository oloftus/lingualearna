(function() {

    var dependencies = [ "linguaApp", "service/crudService", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(dependencies, function(linguaApp, CrudService, ngRegistrationHelper) {

        var NoteService = CrudService;

        ngRegistrationHelper(linguaApp).registerService("noteService",
                [ "jsonWebService", "noteServiceUrl", NoteService ]);
    });
})();
