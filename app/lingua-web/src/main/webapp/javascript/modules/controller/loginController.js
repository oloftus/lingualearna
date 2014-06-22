App.Controller.createNew(function() {

    this.isCalled("loginController");

    this.imports("controller/abstractController");

    this.loads("iframeResizer");
    this.loads("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("$state");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(abstractController) {

        return function($scope, $state, commsPipe) {

            _.extend(this, abstractController);
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
