(function(){
    
    var dependencies = [  "linguaApp", "service/crudService", "util/commonTypes", "service/jsonWebService" ];
    
    define(dependencies, function(linguaApp, CrudService) {
        
        var NoteService = CrudService;
        
        linguaApp.service("noteService", [ "jsonWebService", "noteServiceUrl", NoteService ]);
    });
})();
