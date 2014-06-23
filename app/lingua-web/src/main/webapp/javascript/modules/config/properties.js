App.Module.createNew(function() {

    this.isCalled("properties");
    
    this.hasDefinition(function() {

        with (App.Properties) {
            
            App.Properties.ngViewsRoot = applicationRoot + "/ngViews";

            App.Properties.apiRoot = applicationRoot + "/app/api";
            App.Properties.pagesRoot = applicationRoot + "/app";
            
            App.Properties.translateServiceUrl = apiRoot + "/translate";
            App.Properties.languageNamesServiceUrl = apiRoot + "/languages/langName";
            App.Properties.noteServiceUrl = apiRoot + "/note";
            App.Properties.csrfTokenApiUrl = apiRoot + "/security/csrfToken";
            App.Properties.notebookServiceUrl = apiRoot + "/notebook";
            App.Properties.notesPagesServiceUrl = notebookServiceUrl + "/notebooksPages";
            App.Properties.pingServiceUrl = pagesRoot + "/ping";
            
            App.Properties.csrfSecret = csrfSecret;   
            App.Properties.pageMessagesTimeout = 4000; // Milliseconds
            App.Properties.dialogDisappearTimeout = 4000; // Milliseconds
        }
    });
});
