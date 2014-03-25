define(
[
 "angular",
 "appRoot",
 "config/properties",
 "util/commonTypes",
 "util/util", 
 "service/languageNamesService",
 "service/translateService",
 "controller/translateController"
],
function() {

    linguaApp
    .value("translateServiceUrl", properties.translateServiceUrl).value("languageNamesServiceUrl",
            properties.languageNamesServiceUrl)
    .factory("translateService", [ "jsonWebService", "translateServiceUrl", TranslateService ])
    .factory("languageNamesService", [ "jsonWebService", "languageNamesServiceUrl", LanguageNamesService ])
    .controller("translateController", [ "$scope", "translateService", "languageNamesService", translateController ]);
});
