(function() {

    var imports = [ "ng", "linguaApp", "util/appStates", "underscore" ];

    define(imports, function(ng, linguaApp, appStates, _) {

        var stashProviders = function($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider) {

            linguaApp.controllerProvider = $controllerProvider;
            linguaApp.compileProvider = $compileProvider;
            linguaApp.filterProvider = $filterProvider;
            linguaApp.provide = $provide;
            linguaApp.httpProvider = $httpProvider;
        };

        var AbstractMiniApp = {

            configure : function() {

                var Configuration = function($stateProvider, $sceDelegateProvider, $httpProvider, $controllerProvider,
                        $compileProvider, $filterProvider, $provide) {

                    stashProviders($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider);
                    appStates.setupStates($stateProvider, $sceDelegateProvider);
                };

                linguaApp.config([ "$stateProvider", "$sceDelegateProvider", "$httpProvider", "$controllerProvider",
                        "$compileProvider", "$filterProvider", "$provide", Configuration ]);
            },

            boot : function() {
                ng.bootstrap(document, [ "linguaAppx" ]);
            }
        };

        return AbstractMiniApp;
    });
})();
