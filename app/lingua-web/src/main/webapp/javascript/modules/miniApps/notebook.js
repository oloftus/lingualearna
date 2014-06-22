App.MiniApp.createNew(function() {

    this.isCalled("notebookMiniApp")
    
    this.imports("rootApp");
    this.imports("miniApps/abstractMiniApp");
    this.imports("controller/notebookController");

    this.hasDefinition(function(rootApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        rootApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        rootApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        rootApp.constant("translateServiceUrl", Properties.translateServiceUrl);
        rootApp.constant("notesPagesServiceUrl", Properties.notesPagesServiceUrl);

        this.configure();
        this.boot();
    });
});
