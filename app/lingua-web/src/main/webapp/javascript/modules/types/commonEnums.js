App.TypesDef.createNew(function() {

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
    
    Views = {
        MAIN : "",
        MODAL_DIALOG : "MODAL_DIALOG"
    };
    
    AppStates = {
        READER_MAIN : "READER_MAIN",
        NOTEBOOK_MAIN : "NOTEBOOK_MAIN",
        ADD_NOTE : "ADD_NOTE",
        TRANSLATE : "TRANSLATE",
        LOGIN : "LOGIN",
        SETTINGS : "SETTINGS",
        ADD_NOTEBOOK : "ADD_NOTEBOOK"
    };
    
    Components = {
        ANY : "*",
        READER : "READER",
        TRANSLATE : "TRANSLATE",
        ADD_NOTE : "ADD_NOTE",
        LOGIN : "LOGIN",
        PAGE : "PAGE",
        JSON_WEB_SERVICE : "JSON_WEB_SERVICE"
    };
    
    TranslationSources = {
        GOOGLE: "Google",
        MANUAL: "Manual"
    };
    
    Caches = {
        SUPPORTED_LANGUAGES : "SUPPORTED_LANGUAGES",
        LANGUAGE_NAMES : "LANGUAGE_NAMES"
    };
});
