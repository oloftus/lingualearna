App.Module.createNew(function() {

    this.isCalled("properties");
    
    this.hasDefinition(function() {

        App.Properties.ngViewsRoot = App.Properties.applicationRoot + "/ngViews";

        App.Properties.apiRoot = App.Properties.applicationRoot + "/app/api";
        App.Properties.pagesRoot = App.Properties.applicationRoot + "/app";
        
        App.Properties.translateServiceUrl = App.Properties.apiRoot + "/translate";
        App.Properties.languageNamesServiceUrl = App.Properties.apiRoot + "/languages/langName";
        App.Properties.supportedLanguagesServiceUrl = App.Properties.apiRoot + "/languages/supported";
        App.Properties.noteServiceUrl = App.Properties.apiRoot + "/note";
        App.Properties.csrfTokenApiUrl = App.Properties.apiRoot + "/security/csrfToken";
        App.Properties.notebookServiceUrl = App.Properties.apiRoot + "/notebook";
        App.Properties.notesPagesServiceUrl = App.Properties.notebookServiceUrl + "/notebooksPages";
        App.Properties.pingServiceUrl = App.Properties.pagesRoot + "/ping";
        
        App.Properties.csrfSecret = csrfSecret;   
        App.Properties.pageMessagesTimeout = 4000; // Milliseconds
        App.Properties.dialogDisappearTimeout = 4000; // Milliseconds
    });
});
