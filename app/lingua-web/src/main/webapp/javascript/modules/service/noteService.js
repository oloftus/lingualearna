define([ "util/commonTypes", "appRoot", "util/util", "util/crudService" ], function() {

    var NoteService = CrudService;

    linguaApp.service("noteService", [ "jsonWebService", "noteServiceUrl", NoteService ]);
});
