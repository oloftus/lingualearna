App.Module.createNew(function() {

    this.isCalled("ngRegistrationHelper");
    
    this.imports("underscore");

    this.hasDefinition(function(_) {

        var ComponentType = {
            CONTROLLER : "CONTROLLER",
            SERVICE : "SERVICE",
            DIRECTIVE : "DIRECTIVE",
            FACTORY : "FACTORY"
        };

        var RegisterFuncFactory = function(ngApp) {
            
            this.getFunction = function(componentType) {
                
                var registerFunc;
                
                switch (componentType) {
                    case ComponentType.CONTROLLER:
                        registerFunc = _.isUndefined(ngApp.controllerProvider) ? ngApp.controller
                                : ngApp.controllerProvider.register;
                        break;
                    case ComponentType.SERVICE:
                        registerFunc = _.isUndefined(ngApp.provide) ? ngApp.service : ngApp.provide.service;
                        break;
                    case ComponentType.FACTORY:
                        registerFunc = _.isUndefined(ngApp.provide) ? ngApp.factory : ngApp.provide.factory;
                        break;
                    case ComponentType.DIRECTIVE:
                        registerFunc = _.isUndefined(ngApp.compileProvider) ? ngApp.directive : ngApp.compileProvider.directive;
                        break;
                }
                
                return function(componentName, dependencies, component) {
                    
                    if (!_.isUndefined(component)) {
                        dependencies.push(component);
                    }
                    
                    registerFunc(componentName, dependencies);
                };
            };
        };

        return function(ngApp) {

            var factory = new RegisterFuncFactory(ngApp);
            
            return {
                registerController : factory.getFunction(ComponentType.CONTROLLER),
                registerService : factory.getFunction(ComponentType.SERVICE),
                registerFactory : factory.getFunction(ComponentType.FACTORY),
                registerDirective : factory.getFunction(ComponentType.DIRECTIVE)
            };
        };
    });
});
