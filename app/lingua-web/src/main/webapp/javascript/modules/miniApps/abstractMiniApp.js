(function() {

    var dependencies = [ "ng", "linguaApp", "util/commonTypes", "config/properties" ];

    define(dependencies, function(ng, linguaApp) {

        var AbstractMiniApp = {

            configure : function() {

                var configuration = function($routeProvider, $locationProvider, $sceDelegateProvider,
                        $controllerProvider) {

                    linguaApp.controllerProvider = $controllerProvider;

                    var whitelist = [ Properties.ngViewsRoot + "/addNoteView.html",
                            Properties.ngViewsRoot + "/genericNoteView.html" ];
                    $sceDelegateProvider.resourceUrlWhitelist(whitelist);

                    $locationProvider.html5Mode(true);

                    // TODO: Extract functionality into a closure over viewname & controllers
                    $routeProvider.when(Paths.ADD_NOTE, {
                        templateUrl : Properties.ngViewsRoot + "/addNoteView.html",
                        resolve : {
                            lazyLoadController : function($q, $rootScope) {

                                var deferred = $q.defer();
                                var dependencies = [ Properties.javascriptRoot + "/controller/addNoteController.js" ];

                                require(dependencies, function() {

                                    $rootScope.$apply(function() {

                                        deferred.resolve();
                                    });
                                });

                                return deferred.promise;
                            }
                        }
                    });
                };

                linguaApp.config([ "$routeProvider", "$locationProvider", "$sceDelegateProvider",
                        "$controllerProvider", configuration ]);
            },

            boot : function() {

                ng.bootstrap(document, [ "linguaAppx" ]);
            }
        };

        return AbstractMiniApp;
    });
})();
