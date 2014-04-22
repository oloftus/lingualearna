define([], function() {

    var serverPart = "http://localhost:8080/LinguaWeb/app/api";
    
    return {
        translateServiceUrl : serverPart + "/translate",
        languageNamesServiceUrl : serverPart + "/languages/langName",
        noteServiceUrl: serverPart + "/note"
    };
});
