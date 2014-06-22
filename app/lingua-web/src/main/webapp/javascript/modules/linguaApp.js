App.Module.createNew(function() {

    this.moduleIsCalled("rootApp");

    this.imports("ng");
    this.imports("ui.router");
    this.imports("jquery.ui");
    this.imports("util/commonTypes");
    this.imports("util/commonEnums");
    this.imports("util/messageSignals");
    this.imports("util/messageSubjects");
    this.imports("config/properties");
    this.imports("localization/stringsDefault");
    
    this.hasDefinition(function(ng) {
        return ng.module("linguaAppx", [ "ui.router" ]);
    });
});
