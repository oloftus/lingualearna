App.Module.createNew(function() {

    loadRequireConfig();

    this.moduleIsCalled("notebookMiniApp")
    
    this.imports("linguaApp");
    this.imports("miniApps/abstractMiniApp");
    this.imports("controller/notebookController");

    this.hasDefinition(function(linguaApp, abstractMiniApp) {

        _.extend(this, abstractMiniApp);

        linguaApp.constant("noteServiceUrl", Properties.noteServiceUrl);
        linguaApp.constant("languageNamesServiceUrl", Properties.languageNamesServiceUrl);
        linguaApp.constant("translateServiceUrl", Properties.translateServiceUrl);
        linguaApp.constant("notesPagesServiceUrl", Properties.notesPagesServiceUrl);

        this.configure();
        this.boot();
    });
});
