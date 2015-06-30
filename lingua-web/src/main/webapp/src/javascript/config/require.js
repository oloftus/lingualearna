var require = {
    shim: {
        angular: {
            exports: "angular",
            deps: [
                "jquery"
            ]
        },
        underscore: {
            exports: "_"
        },
        "underscore.string": {
            exports: "str",
            deps: [
                "underscore"
            ]
        },
        jquery: {
            exports: "$"
        },
        "angular-ui-router": {
            exports: "uiRouter",
            deps: [
                "angular"
            ]
        },
        "jquery-ui": {
            deps: [
                "jquery"
            ]
        },
        "angular-contenteditable": {
            exports: "contenteditable",
            deps: [
                "angular"
            ]
        },
        "angular-nanoscroller": {
            deps: [
                "angular",
                "nanoscroller"
            ]
        },
        nanoscroller: {
            deps: [
                "jquery"
            ]
        }
    },
    packages: [

    ],
    paths: {
        // Remove one ../ below for development
        "nanoscroller": "../../dependencies/nanoscroller/bin/javascripts/jquery.nanoscroller"
    }
};
