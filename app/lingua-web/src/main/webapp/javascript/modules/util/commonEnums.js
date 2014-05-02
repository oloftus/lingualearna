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
            BAD_REQUEST : 400
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
            TRANSLATE : "TRANSLATE"
        };
        
        Views = {
            MAIN : "",
            READER_BAR : "readerBar"
        };
        
        Components = {
            ANY : "*",
            READER : "READER",
            TRANSLATE : "TRANSLATE",
            ADD_NOTE : "ADD_NOTE"
        };
    });
})();
