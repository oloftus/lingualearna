App.Controller.createNew(function() {

    this.isCalled("loginController");

    this.loads("iframeResizer");

    this.injects("$scope");
    this.injects("$state");
    this.injects("util/commsPipe");

    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        return function($scope, $state, commsPipe) {

            this.setupDefaultScope($scope);

            iFrameResize({
                enablePublicMethods : true,
                checkOrigin : false,
                heightCalculationMethod : "max",
                messageCallback : function(message) {
                    
                    if (message.message === Signals.LOGIN_SUCCESS) {
                        commsPipe.send(Components.LOGIN, Components.ANY, Signals.LOGIN_SUCCESS);
                    }
                }
            });
        };
    });
});
