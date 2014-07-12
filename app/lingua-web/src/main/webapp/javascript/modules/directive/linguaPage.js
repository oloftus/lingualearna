App.Directive.createNew(function() {

    this.isCalled("linguaPage");
    
    this.hasDefinition(function() {
        
        var VIEW_EXTENSION = ".html";
        
        return function() {
            
            return {
                restrict: "E",
                scope: {
                    page: "=",
                    func : "=",
                    global : "="
                },
                replace: true,
                templateUrl: App.Properties.ngViewsRoot + "/pageDirectiveView" + VIEW_EXTENSION
            };
        }; 
    });
});
