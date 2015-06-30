define(
    [
        "angular",
        "angular-ui-router",
        "angular-contenteditable",
        "jquery-ui",
        "angular-nanoscroller"
    ],

    function (angular) {

        return angular.module("lingua-web", [ "ui.router", "contenteditable", "sun.scrollable" ]);
    });
