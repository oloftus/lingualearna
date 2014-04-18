define([ "appRoot" ], function() {

    addGlobalMessage = function (messageHandler, scope, message, severity) {
        
        messageHandler.clearAllMessages(scope);
        messageHandler.addGlobalMessage(scope, message, severity);
    };
    
    setupDefaultScope = function(scope) {

        scope.model = {};
        scope.func = {};
        scope.model.globalMessages = [];
    };
});
