function doImport() {

    var imports = [];
    for (var i = 0; i < arguments.length; i++) {
        imports = imports.concat(arguments[i]);
    }
    return imports;
};

var App = {};

App.Module = {
    Proto : function() {
        
        this.moduleProps = {};
        
        this.moduleProps._importsFirst = [];
        this.moduleProps._imports = [];
        
        this._importsFirst = function(moduleName) {
            this.moduleProps._importsFirst.push(moduleName);
        };
        
        this.imports = function(moduleName) {
            this.moduleProps._imports.push(moduleName);
        };
        
        this.moduleIsCalled = function(moduleName) {
            this.moduleProps._moduleName = moduleName;
        };

        this.hasDefinition = function(moduleDefinition) {
            define(this.moduleProps._imports, moduleDefinition);
        };
    },
    createNew : function(module) {
        module.prototype = new App.Module.Proto();
        new module();
    }
};

App.NgComponent = {
    Proto : function() {
        
        App.Module.Proto.call(this);
        
        this.moduleProps._ngImports = [];
        this.moduleProps._ngDeps = [];
        
        this.importsNg = function(moduleName) {
            this.moduleProps._ngImports.push(moduleName);
        };
        
        this.dependsOnNg = function(moduleName) {
            this.moduleProps._ngDeps.push(moduleName);
        };
        
        this.hasDefinition = function(componentDefinition) {
            
            var allImports = doImport(this.moduleProps._importsFirst, this.moduleProps._imports, this.moduleProps._ngImports);
            var requireModule = function() {
                
                var linguaApp = Array.prototype.shift.call(arguments);
                var ngRegistrationHelper = Array.prototype.shift.call(arguments);
                var component = componentDefinition.apply(null, arguments);
                this._registerComponent(linguaApp, ngRegistrationHelper, component);
            }.bind(this);
            
            define(allImports, requireModule);
        };
    }
};

App.Factory = {
    Proto : function() {
        
        App.NgComponent.Proto.call(this);
        
        this._importsFirst("linguaApp");
        this._importsFirst("util/ngRegistrationHelper");
        
        var _registerComponent = function(linguaApp, ngRegistrationHelper, factory) {
            ngRegistrationHelper(linguaApp).registerFactory(this.moduleProps._moduleName,
                    this.moduleProps._ngDeps, factory);
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
        
        this._importsFirst("linguaApp");
        this._importsFirst("util/ngRegistrationHelper");
        
        var _registerComponent = function(linguaApp, ngRegistrationHelper, service) {
            ngRegistrationHelper(linguaApp).registerService(this.moduleProps._moduleName,
                    this.moduleProps._ngDeps, service);
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
        
        this._importsFirst("linguaApp");
        this._importsFirst("util/ngRegistrationHelper");
        
        var _registerComponent = function(linguaApp, ngRegistrationHelper, controller) {
            ngRegistrationHelper(linguaApp).registerController(this.moduleProps._moduleName,
                    this.moduleProps._ngDeps, controller);
        };
        
        this._registerComponent = _registerComponent.bind(this);
    },
    createNew : function(module) {

        module.prototype = new this.Proto();
        new module();
    }
};

function loadRequireConfig() {

    Properties = Properties || {};

    with (Properties) {

        Properties.applicationRoot = "http://localhost:8080/LinguaWeb";
        Properties.javascriptRoot = applicationRoot + "/javascript/modules";
    }

    require.config({
        paths : {
            "ng" : "../lib/angular-1.3.0",
            "ui.router" : "../lib/angular-ui-router-0.2.10",
            "underscore" : "../lib/underscore-1.6.0",
            "jquery" : "../lib/jquery-2.1.0",
            "jquery.ui" : "../lib/jquery-ui-1.10.4",
            "iframeResizer" : "../lib/iframeResizer-2.4.3"
        },
        shim : {
            "ng" : {
                exports : "angular"
            },
            "underscore" : {
                exports : "_"
            },
            "jquery" : {
                exports : "$"
            },
            "ui.router" : {
                deps : [ "ng" ]
            },
            "jquery.ui" : {
                deps : [ "jquery" ]
            }
        },
        baseUrl : Properties.javascriptRoot
    });
}
