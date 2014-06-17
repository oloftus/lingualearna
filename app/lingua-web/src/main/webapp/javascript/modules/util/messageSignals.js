(function() {

    var imports = [ "linguaApp" ];

    define(imports, function(linguaApp) {

        Signals = {
            LOGIN_SUCCESS : "login-success",
            CURRENT_NOTEBOOK_CHANGED : "current-notebook-changed",
            CSRF_RETRIEVED : "csrf-retrieved"
        };
    });
})();
