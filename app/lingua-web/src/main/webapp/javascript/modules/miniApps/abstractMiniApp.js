(function() {

    var dependencies = [ "ng", "linguaApp", "util/commonTypes", "config/properties" ];

    define(dependencies, function(ng, linguaApp) {

        var AbstractMiniApp = {

            configure : function(callback) {

                // TODO why do we have to use Properties.ngViewsRoot etc.
                var urlAndViewAndController = function(triggerUrl, viewName, controllerName) {

                    return {
                        url : viewName,
                        templateUrl : Properties.ngViewsRoot + viewName,
                        resolve : {
                            lazyLoadController : function($q, $rootScope) {

                                var deferred = $q.defer();
                                var dependencies = [ Properties.javascriptRoot + controllerName ];

                                require(dependencies, function() {

                                    $rootScope.$apply(function() {

                                        deferred.resolve();
                                    });
                                });

                                return deferred.promise;
                            }
                        }
                    };
                };

                var Configuration = function($stateProvider, $locationProvider, $sceDelegateProvider, $httpProvider,
                        $controllerProvider, $compileProvider, $filterProvider, $provide) {

                    var routingTable = [ {
                        name : Paths.ADD_NOTE,
                        view : "/addNoteView.html",
                        controller : "/controller/addNoteController.js"
                    }, {
                        name : Paths.READER,
                        view : "/reader.html",
                        controller : "/controller/readerController.js"
                    } ];

                    var additionalViews = [ "/genericNoteView.html" ];

                    linguaApp.controllerProvider = $controllerProvider;
                    linguaApp.compileProvider = $compileProvider;
                    linguaApp.filterProvider = $filterProvider;
                    linguaApp.provide = $provide;

                    var whitelist = _.map(additionalViews, function(viewName) {
                        return Properties.ngViewsRoot + viewName;
                    });
                    _.each(routingTable, function(routingEntry) {

                        whitelist.push(routingEntry.view);
                        $stateProvider.state(routingEntry.name, urlAndViewAndController(routingEntry.url, routingEntry.view,
                                routingEntry.controller));
                    });

                    $locationProvider.html5Mode(true);
                    $sceDelegateProvider.resourceUrlWhitelist(whitelist);
                    $httpProvider.defaults.headers.common["X-CSRF-TOKEN"] = pageParam.csrfToken;
                };

                linguaApp.config([ "$stateProvider", "$locationProvider", "$sceDelegateProvider", "$httpProvider",
                        "$controllerProvider", "$compileProvider", "$filterProvider", "$provide", Configuration ]);
            },

            boot : function() {

                ng.bootstrap(document, [ "linguaAppx" ]);
            }
        };

        return AbstractMiniApp;
    });
})();
