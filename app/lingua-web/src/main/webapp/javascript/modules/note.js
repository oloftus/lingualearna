define(
[
 "appRoot",
 "config/properties",
 "service/languageNamesService",
 "service/noteService",
 "controller/noteController"
],
function() {

    linguaApp
    .value("noteServiceUrl", properties.noteServiceUrl)
    .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
    .value("sourceLang", initParams.sourceLang)
    .value("targetLang", initParams.targetLang)
});
