App.Module.createNew(function() {

    this.isCalled("ngRegistrationHelper");
    
    this.imports("underscore");

    this.hasDefinition(function(_) {

        var ComponentType = {
            CONTROLLER : 0,
            SERVICE : 1,
            FACTORY : 2
        };

        registerHelper = function(componentType, ngApp) {

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
            }

            return function(componentName, dependencies, component) {

                if (!_.isUndefined(component)) {
                    dependencies.push(component);
                }

                registerFunc(componentName, dependencies);
            };
        };

        return function(ngApp) {

            return {
                registerController : registerHelper(ComponentType.CONTROLLER, ngApp),
                registerService : registerHelper(ComponentType.SERVICE, ngApp),
                registerFactory : registerHelper(ComponentType.FACTORY, ngApp)
            };
        };
    });
});
