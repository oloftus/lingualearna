define([], function() {

    with (Properties) {
        
        Properties.apiRoot = applicationRoot + "/app/api";
        Properties.translateServiceUrl = apiRoot + "/translate";
        Properties.languageNamesServiceUrl = apiRoot + "/languages/langName";
        Properties.noteServiceUrl = apiRoot + "/note";
        Properties.ngViewsRoot = applicationRoot + "/ngViews";
    }
});
