(function() {

    var imports = [ "underscore" ];

    define(imports, function(_) {

        var ComponentType = {
            CONTROLLER : 0,
            SERVICE : 1,
            FACTORY : 2
        };

        registerHelper = function(componentType, ngApp) {

            switch (componentType) {
                case ComponentType.CONTROLLER:
                    return _.isUndefined(ngApp.controllerProvider) ? ngApp.controller
                            : ngApp.controllerProvider.register;
                case ComponentType.SERVICE:
                    return _.isUndefined(ngApp.provide) ? ngApp.service : ngApp.provide.service;
                case ComponentType.FACTORY:
                    return _.isUndefined(ngApp.provide) ? ngApp.factory : ngApp.provide.factory;
            }
        };

        var registrationHelper = function(ngApp) {

            return {
                registerController : registerHelper(ComponentType.CONTROLLER, ngApp),
                registerService : registerHelper(ComponentType.SERVICE, ngApp),
                registerFactory : registerHelper(ComponentType.FACTORY, ngApp)
            };
        };

        return registrationHelper;
    });
})();
