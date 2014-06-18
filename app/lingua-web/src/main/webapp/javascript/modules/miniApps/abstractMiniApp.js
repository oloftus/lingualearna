(function() {

    var imports = [];
    
    imports.push("ng");
    imports.push("linguaApp");
    imports.push("util/appStates");
    imports.push("underscore");

    define(doImport(imports), function(ng, linguaApp, appStates, _) {

        var applyJsOverrides = function() {
            
            if (typeof String.prototype.endsWith !== 'function') {
                String.prototype.endsWith = function(suffix) {
                    return this.indexOf(suffix, this.length - suffix.length) !== -1;
                };
            }
        };
        
        var stashProviders = function($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider) {

            linguaApp.controllerProvider = $controllerProvider;
            linguaApp.compileProvider = $compileProvider;
            linguaApp.filterProvider = $filterProvider;
            linguaApp.provide = $provide;
            linguaApp.httpProvider = $httpProvider;
        };
        
        var configure = function() {

            applyJsOverrides();

            var Configuration = function($stateProvider, $sceDelegateProvider, $httpProvider, $controllerProvider,
                    $compileProvider, $filterProvider, $provide) {

                stashProviders($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider);
                appStates.setupStates($stateProvider, $sceDelegateProvider);
            };

            linguaApp.config([ "$stateProvider", "$sceDelegateProvider", "$httpProvider", "$controllerProvider",
                    "$compileProvider", "$filterProvider", "$provide", Configuration ]);
        };

        var boot = function() {
            
            ng.bootstrap(document, [ "linguaAppx" ]);
        };

        var AbstractMiniApp = {

            configure : configure,
            boot : boot
        };

        return AbstractMiniApp;
    });
})();
