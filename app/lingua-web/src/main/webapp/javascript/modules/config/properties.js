App.Module.createNew(function() {

    this.isCalled("properties");
    
    this.imports("rootApp");

    this.hasDefinition(function(rootApp) {

        with (Properties) {
            
            Properties.ngViewsRoot = applicationRoot + "/ngViews";

            Properties.apiRoot = applicationRoot + "/app/api";
            Properties.pagesRoot = applicationRoot + "/app";
            
            Properties.translateServiceUrl = apiRoot + "/translate";
            Properties.languageNamesServiceUrl = apiRoot + "/languages/langName";
            Properties.noteServiceUrl = apiRoot + "/note";
            Properties.csrfTokenApiUrl = apiRoot + "/security/csrfToken";
            Properties.notebookServiceUrl = apiRoot + "/notebook";
            Properties.notesPagesServiceUrl = notebookServiceUrl + "/notebooksPages";
            Properties.pingServiceUrl = pagesRoot + "/ping";
            
            Properties.csrfSecret = csrfSecret;   
            Properties.pageMessagesTimeout = 4000; // Milliseconds
            Properties.dialogDisappearTimeout = 4000; // Milliseconds
        }
    });
});
