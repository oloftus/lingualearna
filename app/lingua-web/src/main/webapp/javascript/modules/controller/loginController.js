App.Controller.createNew(function() {

    this.moduleIsCalled("loginController");

    this.imports("linguaApp");
    this.imports("controller/abstractController");
    this.imports("util/ngRegistrationHelper");

    this.importsNg("iframeResizer");
    this.importsNg("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("$state");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(linguaApp, abstractController, ngRegistrationHelper) {

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
