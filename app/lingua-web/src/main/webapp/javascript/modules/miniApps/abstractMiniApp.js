(function() {

    var dependencies = [ "ng", "linguaApp", "underscore" ];

    define(dependencies, function(ng, linguaApp, _) {

        var VIEW_EXTENSION = ".html";
        var JS_EXTENSION = ".js";
        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        var whitelist = [];

        var routingTable = [ {
            stateName : AppStates.MAIN,
            views : [ {
                viewName : Views.READER_BAR,
                viewUrl : "/readerBarView"
            } ],
            controllers : [ "/controller/readerController" ]
        }, {
            stateName : AppStates.ADD_NOTE,
            views : [ {
                viewName : Views.READER_BAR,
                viewUrl : "/readerBarView"
            }, {
                viewName : Views.MAIN,
                viewUrl : "/addNoteView"
            } ],
            controllers : [ "/controller/readerController", "/controller/addNoteController" ]
        }, {
            stateName : AppStates.TRANSLATE,
            views : [ {
                viewName : Views.READER_BAR,
                viewUrl : "/readerBarView"
            }, {
                viewName : Views.MAIN,
                viewUrl : "/translateView"
            } ],
            controllers : [ "/controller/readerController", "/controller/translateController" ]
        } ];

        var additionalViews = [ "/abstractNoteView" ];

        var viewsAndControllers = function(views, controllers) {

            var absoluteViews = {};
            _.each(views, function(view) {
                absoluteViews[view.viewName] = {
                    templateUrl : Properties.ngViewsRoot + view.viewUrl + VIEW_EXTENSION
                };
            });

            var absoluteControllers = _.map(controllers, function(controller) {
                return Properties.javascriptRoot + controller + JS_EXTENSION;
            });

            return {
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

        var stashProviders = function($controllerProvider, $compileProvider, $filterProvider, $provide) {

            linguaApp.controllerProvider = $controllerProvider;
            linguaApp.compileProvider = $compileProvider;
            linguaApp.filterProvider = $filterProvider;
            linguaApp.provide = $provide;
        };

        var addAdditionalViewsToWhitelist = function() {
            
            _.map(additionalViews, function(viewName) {
                return Properties.ngViewsRoot + viewName + VIEW_EXTENSION;
            });
        };

        var addViewsToWhitelist = function(views) {

            _.each(views, function(view) {
                whitelist.push(view.viewUrl);
            });
        };

        var registerState = function($stateProvider, routingEntry) {

            $stateProvider.state(routingEntry.stateName, viewsAndControllers(routingEntry.views,
                    routingEntry.controllers));
        };
        
        var setupStates = function($stateProvider, $sceDelegateProvider) {

            _.each(routingTable, function(routingEntry) {
                addViewsToWhitelist(routingEntry.views);
                registerState($stateProvider, routingEntry);
            });

            addAdditionalViewsToWhitelist();
            
            $sceDelegateProvider.resourceUrlWhitelist(whitelist);
        };

        var AbstractMiniApp = {

            configure : function() {

                var Configuration = function($stateProvider, $sceDelegateProvider, $httpProvider, $controllerProvider,
                        $compileProvider, $filterProvider, $provide) {

                    stashProviders($controllerProvider, $compileProvider, $filterProvider, $provide);
                    setupStates($stateProvider, $sceDelegateProvider);
                    $httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = pageParam.csrfToken;
                };

                linguaApp.config([ "$stateProvider", "$sceDelegateProvider", "$httpProvider", "$controllerProvider",
                        "$compileProvider", "$filterProvider", "$provide", Configuration ]);
            },

            boot : function() {
                ng.bootstrap(document, [ "linguaAppx" ]);
            }
        };

        return AbstractMiniApp;
    });
})();
