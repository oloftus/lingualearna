(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        HttpMethod = {
            POST : "POST",
            GET : "GET",
            PUT : "PUT",
            DELETE : "DELETE"
        };

        HttpHeaders = {
            BAD_REQUEST : 400,
            FORBIDDEN : 403,
            INTERNAL_SERVER_ERROR: 500
        };

        MessageSeverity = {
            INFO : "INFO",
            ERROR : "ERROR"
        };
        
        Dialogs = {
            ADD_NOTE : "ADD_NOTE"
        };
        
        AppStates = {
            MAIN : "MAIN",
            ADD_NOTE : "ADD_NOTE",
            TRANSLATE : "TRANSLATE",
            LOGIN : "LOGIN"
        };
        
        Views = {
            MAIN : ""
        };
        
        Components = {
            ANY : "*",
            READER : "READER",
            TRANSLATE : "TRANSLATE",
            ADD_NOTE : "ADD_NOTE",
            LOGIN : "LOGIN",
            PAGE : "PAGE"
        };
        
        TranslationSources = {
            GOOGLE: "Google",
            MANUAL: "Manual"
        };
    });
})();
