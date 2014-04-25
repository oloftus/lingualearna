(function(){
    
    var dependencies = [ "linguaApp", "jquery", "util/commonTypes" ];
    
    define(dependencies, function(linguaApp, $) {
        
        var DialogFactory = function() {

            var renderDialog = function() {
                
                var controllerRoot = $("<div ng-controller='dialogController' />")
                var ngInclude = $("<ng-include class='lingua-ng-include' />");
                controllerRoot.append(ngInclude);
            };
            
            var newDialog = function(dialogName, parameters) {
                
                if (!(dialogName in Dialogs)) {
                    throw new IllegalArgumentException("Invalid dialog name '" + dialogName + "'")
                }
                
                alert("hello");
//                renderDialog();
            };
            
            return {
                newDialog : newDialog
            };
        };
        
        linguaApp.service("dialogFactory", [ DialogFactory ]);
    });
})();
