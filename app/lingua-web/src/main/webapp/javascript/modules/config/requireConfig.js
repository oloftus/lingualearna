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
        
        this.isCalled = function(componentName) {
            this.moduleProps._componentName = componentName;
        };
    }
};

App.Controller = {
    Proto : function() {
        
        App.NgComponent.Proto.call(this);
        
        this._importsFirst("linguaApp");
        this._importsFirst("util/ngRegistrationHelper");
        
        this.hasDefinition = function(controllerDefinition) {
            
            var allImports = doImport(this.moduleProps._importsFirst, this.moduleProps._imports, this.moduleProps._ngImports);
            var requireModule = function() {
                
                var linguaApp = Array.prototype.shift.call(arguments);
                var ngRegistrationHelper = Array.prototype.shift.call(arguments);
                
                var controller = controllerDefinition.apply(null, arguments);
                
                ngRegistrationHelper(linguaApp).registerController(this.moduleProps._componentName,
                        this.moduleProps._ngDeps, controller);
            }.bind(this);
            
            define(allImports, requireModule);
        };
    },
    createNew : function(module) {

        module.prototype = new App.Controller.Proto();
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
