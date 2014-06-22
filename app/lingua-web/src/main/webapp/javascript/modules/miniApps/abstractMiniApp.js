App.Module.createNew(function() {

    this.moduleIsCalled("abstractMiniApp");
    
    this.imports("ng");
    this.imports("linguaApp");
    this.imports("util/appStates");
    this.imports("underscore");

    this.hasDefinition(function(ng, linguaApp, appStates, _) {

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

        return {
            configure : configure,
            boot : boot
        };
    });
});
