App.Controller.createNew(function() {

    this.isCalled("loginController");

    this.imports("rootApp");
    this.imports("controller/abstractController");

    this.importsNg("iframeResizer");
    this.importsNg("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("$state");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(rootApp, abstractController) {

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
