define(
[
 "appRoot",
 "config/properties",
 "localization/stringsDefault",
 "service/languageNamesService",
 "service/noteService",
 "controller/editNoteController",
],
function() {

    linguaApp
    .value("noteServiceUrl", properties.noteServiceUrl)
    .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
    .value("noteId", initParams.noteId);
    
    angular.bootstrap(document, ["linguaApp"]);
});
