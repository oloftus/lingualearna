App.MiniApp.createNew(function() {

    this.isCalled("readerMiniApp");
    
    this.imports("framework/rootApp");
    this.imports("miniApps/abstractMiniApp");
    
    this.loads("controller/readerController");
    
    this.hasDefinition(function(rootApp, abstractMiniApp) {

        App.Properties.csrfSecret = csrfSecret;
        
        rootApp.constant("noteServiceUrl", App.Properties.noteServiceUrl);
        rootApp.constant("languageNamesServiceUrl", App.Properties.languageNamesServiceUrl);
        rootApp.constant("supportedLanguagesServiceUrl", App.Properties.supportedLanguagesServiceUrl);
        rootApp.constant("translateServiceUrl", App.Properties.translateServiceUrl);
        rootApp.constant("notesPagesServiceUrl", App.Properties.notesPagesServiceUrl);
        rootApp.constant("notebookServiceUrl", App.Properties.notebookServiceUrl);

        abstractMiniApp.configure();
        abstractMiniApp.boot();
    });
});
