App.loadRequireConfig = function() {

    App.Properties.applicationRoot = "http://localhost:8080/LinguaWeb";
    App.Properties.javascriptRoot = App.Properties.applicationRoot + "/javascript/modules";

    require.config({
        paths : {
            "ng" : "../lib/angular-1.3.0",
            "ui.router" : "../lib/angular-ui-router-0.2.10",
            "underscore" : "../lib/underscore-1.6.0",
            "jquery" : "../lib/jquery-2.1.0",
            "jquery.ui" : "../lib/jquery-ui-1.10.4",
            "iframeResizer" : "../lib/iframeResizer-2.4.3"
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
            },
            "jquery.ui" : {
                deps : [ "jquery" ]
            }
        },
        baseUrl : App.Properties.javascriptRoot
    });
};
