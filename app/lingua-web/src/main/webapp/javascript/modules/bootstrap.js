loadRequireConfig();

define([ "angular", "linguaApp" ], function(angular, linguaApp) {

    linguaApp.register();
    angular.bootstrap(document, ["linguaApp"]);
});

