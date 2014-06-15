(function() {

    var dependencies = [ "ng", "linguaApp", "underscore" ];

    define(dependencies, function(ng, linguaApp, _) {

        var VIEW_EXTENSION = ".html";
        var JS_EXTENSION = ".js";

        var whitelist = [];

        var routingTable = [ {
            stateName : AppStates.READER_MAIN
        }, {
            stateName : AppStates.NOTEBOOK_MAIN,
            views : [ {
                viewUrl : "/binderPanelView"
            } ],
            controllers : [ "/controller/binderController" ]
        }, {
            stateName : AppStates.ADD_NOTE,
            views : [ {
                viewName : Views.MODAL_DIALOG,
                viewUrl : "/addNoteDialogView"
            } ],
            controllers : [ "/controller/addNoteController" ]
        }, {
            stateName : AppStates.TRANSLATE,
            views : [ {
                viewName : Views.MODAL_DIALOG,
                viewUrl : "/translateDialogView"
            } ],
            controllers : [ "/controller/translateController" ]
        }, {
            stateName : AppStates.LOGIN,
            views : [ {
                viewName : Views.MODAL_DIALOG,
                viewUrl : "/loginDialogView"
            } ],
            controllers : [ "/controller/loginController" ]
        } ];

        var additionalViews = [ "/abstractNoteDialogView", "/notebookHeaderView" ];
        var additionalWhitelistUrls = [ Properties.pagesRoot + "/login" ];

        var viewsAndControllers = function(views, controllers) {
            
            views = views || [];
            controllers = controllers || [];

            var absoluteViews = {};
            _.each(views, function(view) {
                if (_.isUndefined(view.viewName)) {
                    view.viewName = Views.MAIN;
                }
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

        var stashProviders = function($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider) {

            linguaApp.controllerProvider = $controllerProvider;
            linguaApp.compileProvider = $compileProvider;
            linguaApp.filterProvider = $filterProvider;
            linguaApp.provide = $provide;
            linguaApp.httpProvider = $httpProvider;
        };

        var addAdditionalViewsToWhitelist = function() {
            
            var additionalViewUrls = _.map(additionalViews, function(viewUrl) {
                return Properties.ngViewsRoot + viewUrl + VIEW_EXTENSION;
            });
            whitelist = _.union(whitelist, additionalViewUrls, additionalWhitelistUrls);
        };

        var addViewsToWhitelist = function(views) {

            _.each(views, function(view) {
                whitelist.push(Properties.ngViewsRoot + view.viewUrl + VIEW_EXTENSION);
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

                    stashProviders($controllerProvider, $compileProvider, $filterProvider, $provide, $httpProvider);
                    setupStates($stateProvider, $sceDelegateProvider);
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
