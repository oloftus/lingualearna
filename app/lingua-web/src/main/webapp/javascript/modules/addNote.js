loadRequireConfig();

define(
[
 "appRoot",
 "config/properties",
 "localization/stringsDefault",
 "service/languageNamesService",
 "service/noteService",
 "controller/addNoteController",
],
function() {

    var formInput = {
        foreignLang: initParams.foreignLang,
        localLang: initParams.localLang,
        foreignNote: initParams.foreignNote,
        localNote: initParams.localNote,
        additionalNotes: initParams.additionalNotes,
        sourceUrl: initParams.sourceUrl,
        noteId: initParams.noteId
    };
    
    linguaApp
    .value("noteServiceUrl", properties.noteServiceUrl)
    .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
    .value("formInput", formInput);
    
    angular.bootstrap(document, ["linguaApp"]);
});
