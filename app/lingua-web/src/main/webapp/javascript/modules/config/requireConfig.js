require.config({
    paths : {
        "angular" : "../lib/angular-max",
        "underscore" : "../lib/underscore",
        "jquery" : "../lib/jquery-max",
        "underscore.string" : "../lib/underscore.string",
    // "angular": "https://ajax.googleapis.com/ajax/libs/angularjs/1.3.0-beta.5/angular.min",
    // "jquery": "https://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min"
    },

    shim : {
        "underscore.string" : {
            deps : [ "underscore" ]
        }
    }
});
