(function(){
    
    var dependencies = [  "linguaApp", "util/crudService", "util/commonTypes", "util/util" ];
    
    define(dependencies, function(linguaApp, CrudService) {
        
        var NoteService = CrudService;
        
        linguaApp.service("noteService", [ "jsonWebService", "noteServiceUrl", NoteService ]);
    });
})();
