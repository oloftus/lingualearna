App.Module.createNew(function() {

    this.isCalled("rootApp");

    this.imports("ng");
    this.imports("ui.router");
    this.imports("jquery.ui");
    this.imports("types/commonTypes");
    this.imports("types/commonEnums");
    this.imports("types/messageSignals");
    this.imports("types/messageSubjects");
    this.imports("config/properties");
    this.imports("localization/stringsDefault");
    
    this.hasDefinition(function(ng, rootApp) {
        return ng.module("rootAppx", [ "ui.router" ]);
    });
});
