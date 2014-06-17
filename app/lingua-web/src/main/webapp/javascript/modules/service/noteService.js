(function() {

    var imports = [ "linguaApp", "service/crudService", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(imports, function(linguaApp, CrudService, ngRegistrationHelper) {

        var NoteService = CrudService;

        ngRegistrationHelper(linguaApp).registerService("noteService",
                [ "jsonWebService", "noteServiceUrl", NoteService ]);
    });
})();
