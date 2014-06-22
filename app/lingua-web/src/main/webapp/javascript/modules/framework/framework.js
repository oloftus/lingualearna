var App = {};

App.Module = {
    Proto : function() {

        this.moduleProps = {};

        this.moduleProps._firstImports = [];
        this.moduleProps._firstLoads = [];
        this.moduleProps._imports = [];
        this.moduleProps._loads = [];

        this._doImport = function() {

            var imports = [];
            for (var i = 0; i < arguments.length; i++) {
                imports = imports.concat(arguments[i]);
            }
            return imports;
        };

        this._firstImports = function(moduleName) {
            this.moduleProps._firstImports.push(moduleName);
        };

        this._firstLoads = function(moduleName) {
            this.moduleProps._firstLoads.push(moduleName);
        };
        
        this.isCalled = function(moduleName) {
            this.moduleProps._moduleName = moduleName;
        };

        this.imports = function(moduleName) {
            this.moduleProps._imports.push(moduleName);
        };

        this.loads = function(moduleName) {
            this.moduleProps._loads.push(moduleName);
        };

        this.hasDefinition = function(moduleDefinition) {

            var allImports = this._doImport(this.moduleProps._firstImports, this.moduleProps._imports,
                    this.moduleProps._loads, this.moduleProps._firstLoads);
            define(allImports, moduleDefinition);
        };
        
        this._firstLoads("framework/rootApp");
    },
    createNew : function(module) {
        module.prototype = new this.Proto();
        new module();
    }
};

App.TypesDef = {
    createNew : function(module) {
        new module();
    }
};

App.MiniApp = {
    createNew : function(module) {
        App.loadRequireConfig();
        module.prototype = new App.Module.Proto();
        new module();
    }
};

App.NgComponent = {
    Proto : function() {

        App.Module.Proto.call(this);

        this.moduleProps._ngDeps = [];

        this.dependsOnNg = function(moduleName) {
            this.moduleProps._ngDeps.push(moduleName);
        };

        this.hasDefinition = function(componentDefinition) {

            var allImports = this._doImport(this.moduleProps._firstImports, this.moduleProps._imports,
                    this.moduleProps._loads, this.moduleProps._firstLoads);
            var requireModule = function() {

                var rootApp = Array.prototype.shift.call(arguments);
                var ngRegistrationHelper = Array.prototype.shift.call(arguments);
                var component = componentDefinition.apply(null, arguments);
                this._registerComponent(rootApp, ngRegistrationHelper, component);
            }.bind(this);

            define(allImports, requireModule);
        };
        
        this._firstImports("framework/rootApp");
        this._firstImports("framework/ngRegistrationHelper");
    }
};

App.Factory = {
    Proto : function() {

        App.NgComponent.Proto.call(this);

        var _registerComponent = function(rootApp, ngRegistrationHelper, factory) {
            ngRegistrationHelper(rootApp).registerFactory(this.moduleProps._moduleName, this.moduleProps._ngDeps,
                    factory);
        };

        this._registerComponent = _registerComponent.bind(this);
    },
    createNew : function(module) {

        module.prototype = new this.Proto();
        new module();
    }
};

App.Service = {
    Proto : function() {

        App.NgComponent.Proto.call(this);

        var _registerComponent = function(rootApp, ngRegistrationHelper, service) {
            ngRegistrationHelper(rootApp).registerService(this.moduleProps._moduleName, this.moduleProps._ngDeps,
                    service);
        };

        this._registerComponent = _registerComponent.bind(this);
    },
    createNew : function(module) {

        module.prototype = new this.Proto();
        new module();
    }
};

App.Controller = {
    Proto : function() {

        App.NgComponent.Proto.call(this);

        var _registerComponent = function(rootApp, ngRegistrationHelper, controller) {
            ngRegistrationHelper(rootApp).registerController(this.moduleProps._moduleName, this.moduleProps._ngDeps,
                    controller);
        };

        this._registerComponent = _registerComponent.bind(this);
    },
    createNew : function(module) {

        module.prototype = new this.Proto();
        new module();
    }
};
