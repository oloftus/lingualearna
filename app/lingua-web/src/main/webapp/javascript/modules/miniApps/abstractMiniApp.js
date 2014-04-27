(function() {

    var dependencies = [ "ng", "linguaApp", "underscore", "util/commonTypes", "config/properties" ];

    define(dependencies, function(ng, linguaApp, _) {

        var AbstractMiniApp = {

            configure : function(callback) {

                var viewsAndControllers = function(stateUrl, views, controllers) {

                    var absoluteViews = {};
                    _.each(views, function(view) {
                        absoluteViews[view.viewName] = {
                            templateUrl : Properties.ngViewsRoot + view.viewUrl
                        };
                    });

                    var absoluteControllers = _.map(controllers, function(controller) {
                        return Properties.javascriptRoot + controller;
                    });

                    return {
                        url : stateUrl,
                        views : absoluteViews,
                        resolve : {
                            lazyLoadController : function($q, $rootScope) {

                                var deferred = $q.defer();
                                var dependencies = absoluteControllers;

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
                        stateName : AppStates.MAIN,
                        views : [ {
                            viewName : "readerBar",
                            viewUrl : "/readerBarView.html"
                        } ],
                        controllers : [ "/controller/readerController.js" ]
                    }, {
                        stateName : AppStates.ADD_NOTE,
                        views : [ {
                            viewName : "readerBar",
                            viewUrl : "/readerBarView.html"
                        }, {
                            viewName : "",
                            viewUrl : "/addNoteView.html"
                        } ],
                        controllers : [ "/controller/readerController.js", "/controller/addNoteController.js" ]
                    } ];

                    var additionalViews = [ "/abstractNoteView.html" ];

                    linguaApp.controllerProvider = $controllerProvider;
                    linguaApp.compileProvider = $compileProvider;
                    linguaApp.filterProvider = $filterProvider;
                    linguaApp.provide = $provide;

                    var whitelist = _.map(additionalViews, function(viewName) {
                        return Properties.ngViewsRoot + viewName;
                    });
                    _.each(routingTable, function(routingEntry) {

                        _.each(routingEntry.views, function(view) {
                            whitelist.push(view.viewUrl);
                        });
                        $stateProvider.state(routingEntry.stateName, viewsAndControllers(routingEntry.stateUrl,
                                routingEntry.views, routingEntry.controllers));
                    });

                    $stateProvider.state("*", {
                        children : [ {
                            name : AppStates.MAIN
                        } ]
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
