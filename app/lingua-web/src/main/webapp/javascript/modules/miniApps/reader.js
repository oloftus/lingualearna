App.MiniApp.createNew(function() {

    this.isCalled("readerMiniApp");
    
    this.imports("framework/rootApp");
    this.imports("miniApps/abstractMiniApp");
    
    this.loads("controller/readerController");
    
    this.hasDefinition(function(rootApp, abstractMiniApp) {

        rootApp.constant("noteServiceUrl", App.Properties.noteServiceUrl);
        rootApp.constant("languageNamesServiceUrl", App.Properties.languageNamesServiceUrl);
        rootApp.constant("translateServiceUrl", App.Properties.translateServiceUrl);
        rootApp.constant("notesPagesServiceUrl", App.Properties.notesPagesServiceUrl);

        abstractMiniApp.configure();
        abstractMiniApp.boot();
    });
});
