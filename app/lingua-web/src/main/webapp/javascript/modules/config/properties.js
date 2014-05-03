(function() {

    var dependencies = [ "linguaApp" ];

    define(dependencies, function(linguaApp) {

        with (Properties) {
            
            Properties.ngViewsRoot = applicationRoot + "/ngViews";

            Properties.apiRoot = applicationRoot + "/app/api";
            Properties.pagesRoot = applicationRoot + "/app";
            Properties.translateServiceUrl = apiRoot + "/translate";
            Properties.languageNamesServiceUrl = apiRoot + "/languages/langName";
            Properties.noteServiceUrl = apiRoot + "/note";
            Properties.csrfTokenApiUrl = apiRoot + "/security/csrfToken";
            Properties.loginSuccessSignal = "lingua-login-success";
        }
    });
})();
