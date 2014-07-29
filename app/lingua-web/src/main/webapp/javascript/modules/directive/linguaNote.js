App.Directive.createNew(function() {

    this.isCalled("linguaNote");
    
    this.hasDefinition(function() {
        
        var VIEW_EXTENSION = ".html";
        
        return function() {
            
            return {
                restrict: "E", // Match only element names
                scope: {
                    note: "=",
                    func : "="
                },
                replace: true,
                templateUrl: App.Properties.ngViewsRoot + "/noteDirectiveView" + VIEW_EXTENSION
            };
        }; 
    });
});
