define([], function() {

    var serverPart = "http://localhost:8080/LinguaWeb/app/api";
    
    properties = {

        translateServiceUrl : serverPart + "/translation/translate",
        languageNamesServiceUrl : serverPart + "/translation/languageName",
        noteServiceUrl: serverPart + "/note/add"
    };
});
