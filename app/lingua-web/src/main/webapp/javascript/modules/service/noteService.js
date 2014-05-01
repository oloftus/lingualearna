(function(){
    
    var dependencies = [  "linguaApp", "service/crudService", "service/jsonWebService" ];
    
    define(dependencies, function(linguaApp, CrudService) {
        
        var NoteService = CrudService;
        
        linguaApp.provide.service("noteService", [ "jsonWebService", "noteServiceUrl", NoteService ]);
    });
})();
