function loadRequireConfig() {
    
    require.config({
        paths : {
            "angular" : "../lib/angular-1.3.0",
            "underscore" : "../lib/underscore-1.6.0",
            "jquery" : "../lib/jquery-2.1.0",
            "underscore.string" : "../lib/underscore.string-2.3.0"
        },
        shim : {
            "angular" : {
                exports : "angular"
            },
            "underscore" : {
                exports : "_"
            },
            "jquery" : {
                exports : "$"
            }
        },
        baseUrl : "/LinguaWeb/javascript/modules/"
    });
}
