App.Directive.createNew(function() {

    this.isCalled("linguaNote");
    
    this.hasDefinition(function() {
        
        var VIEW_EXTENSION = ".html";
        
        return function() {
            
            return {
            restrict: "E",
            scope: {
                note: "=note"
            },
            templateUrl: App.Properties.ngViewsRoot + "/noteView" + VIEW_EXTENSION
            };
        }; 
    });
});