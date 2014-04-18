define(
[
 "appRoot",
 "config/properties",
 "service/languageNamesService",
 "service/translateService",
 "controller/translateController"
],
function() {

    linguaApp
    .value("translateServiceUrl", properties.translateServiceUrl)
    .value("languageNamesServiceUrl", properties.languageNamesServiceUrl)
    .value("sourceLang", initParams.sourceLang)
    .value("targetLang", initParams.targetLang)
    .value("initQuery", initParams.query);

    angular.bootstrap(document, ["linguaApp"]);
});
