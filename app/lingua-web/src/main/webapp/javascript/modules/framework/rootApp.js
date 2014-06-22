App.Module.createNew(function() {

    this.isCalled("rootApp");

    this.imports("ng");
    
    this.loads("ui.router");
    this.loads("jquery.ui");
    this.loads("types/commonTypes");
    this.loads("types/commonEnums");
    this.loads("types/messageSignals");
    this.loads("types/messageSubjects");
    this.loads("config/properties");
    this.loads("localization/stringsDefault");
    
    this.hasDefinition(function(ng) {
        return ng.module("rootAppx", [ "ui.router" ]);
    });
});
