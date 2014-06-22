App.Module.createNew(function() {

    this.isCalled("abstractMiniApp");
    
    this.imports("ng");
    this.imports("rootApp");
    this.imports("util/appStates");
    this.imports("underscore");

    this.hasDefinition(function(ng, rootApp, appStates, _) {

        var applyJsOverrides = function() {
            
            if (typeof String.prototype.endsWith !== 'function') {
                String.prototype.endsWith = function(suffix) {
                    return this.indexOf(suffix, this.length - suffix.length) !== -1;
                };
            }
        };
        
        var stashProviders = function($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider) {

            rootApp.controllerProvider = $controllerProvider;
            rootApp.compileProvider = $compileProvider;
            rootApp.filterProvider = $filterProvider;
            rootApp.provide = $provide;
            rootApp.httpProvider = $httpProvider;
        };
        
        var configure = function() {

            applyJsOverrides();

            var Configuration = function($stateProvider, $sceDelegateProvider, $httpProvider, $controllerProvider,
                    $compileProvider, $filterProvider, $provide) {

                stashProviders($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider);
                appStates.setupStates($stateProvider, $sceDelegateProvider);
            };

            rootApp.config([ "$stateProvider", "$sceDelegateProvider", "$httpProvider", "$controllerProvider",
                    "$compileProvider", "$filterProvider", "$provide", Configuration ]);
        };

        var boot = function() {
            
            ng.bootstrap(document, [ "rootAppx" ]);
        };

        return {
            configure : configure,
            boot : boot
        };
    });
});
