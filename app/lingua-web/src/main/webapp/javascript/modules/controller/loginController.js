(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "iframeResizer",
            "util/commsPipe" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper) {

            var LoginController = function($scope, $state, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            iFrameResize({
                enablePublicMethods : true,
                checkOrigin : false,
                heightCalculationMethod : "max",
                messageCallback : function(message) {
                    if (message.message === Signals.loginSuccessSignal) {
                        commsPipe.send(Components.LOGIN, Components.ANY, Signals.loginSuccessSignal);
                    }
                }
            });
        };

        ngRegistrationHelper(linguaApp).registerController("loginController",
                [ "$scope", "$state", "commsPipe", LoginController ]);
    });
})();
