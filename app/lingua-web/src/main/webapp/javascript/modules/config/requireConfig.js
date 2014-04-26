function loadRequireConfig() {
    
    Properties = {};
    
    with (Properties) {
        
        Properties.applicationRoot = "http://localhost:8080/LinguaWeb";
        Properties.javascriptRoot = applicationRoot + "/javascript/modules";
    }
    
    require.config({
        paths : {
            "ng" : "../lib/angular-1.3.0",
            "ui.router" : "../lib/angular-ui-router-0.2.10",
            "underscore" : "../lib/underscore-1.6.0",
            "jquery" : "../lib/jquery-2.1.0",
            "underscore.string" : "../lib/underscore.string-2.3.0"
        },
        shim : {
            "ng" : {
                exports : "angular"
            },
            "underscore" : {
                exports : "_"
            },
            "jquery" : {
                exports : "$"
            },
            "ui.router" : {
                deps : [ "ng" ]
            }
        },
        baseUrl : Properties.javascriptRoot
    });
}
