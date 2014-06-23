App.MiniApp.createNew(function() {

    this.isCalled("notebookMiniApp")
    
    this.imports("framework/rootApp");
    this.imports("miniApps/abstractMiniApp");
    
    this.loads("controller/notebookController");

    this.hasDefinition(function(rootApp, abstractMiniApp) {

        rootApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        rootApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        rootApp.constant("translateServiceUrl", Properties.translateServiceUrl);
        rootApp.constant("notesPagesServiceUrl", Properties.notesPagesServiceUrl);

        abstractMiniApp.configure();
        abstractMiniApp.boot();
    });
});
