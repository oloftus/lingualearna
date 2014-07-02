App.MiniApp.createNew(function() {

    this.isCalled("notebookMiniApp");
    
    this.imports("framework/rootApp");
    this.imports("miniApps/abstractMiniApp");
    
    this.loads("controller/notebookRootController");

    this.hasDefinition(function(rootApp, abstractMiniApp) {

        App.Properties.csrfToken = csrfToken;
        
        rootApp.constant("noteServiceUrl", App.Properties.noteServiceUrl);
        rootApp.constant("languageNamesServiceUrl", App.Properties.languageNamesServiceUrl);
        rootApp.constant("supportedLanguagesServiceUrl", App.Properties.supportedLanguagesServiceUrl);
        rootApp.constant("translateServiceUrl", App.Properties.translateServiceUrl);
        rootApp.constant("notesPagesServiceUrl", App.Properties.notesPagesServiceUrl);
        rootApp.constant("notebookServiceUrl", App.Properties.notebookServiceUrl);
        rootApp.constant("notesByPageServiceUrl", App.Properties.notesByPageServiceUrl);
        rootApp.constant("pageServiceUrl", App.Properties.pageServiceUrl);

        abstractMiniApp.configure();
        abstractMiniApp.boot();
    });
});
