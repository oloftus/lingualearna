App.Directive.createNew(function() {

    this.isCalled("linguaNote");
    
    this.hasDefinition(function() {
        
        var VIEW_EXTENSION = ".html";
        
        return function() {
            
            return {
                restrict: "E",
                scope: {
                    note: "=",
                    func : "="
                },
                replace: true,
                templateUrl: App.Properties.ngViewsRoot + "/noteView" + VIEW_EXTENSION
            };
        }; 
    });
});
