App.Module.createNew(function() {

    this.isCalled("appStates");

    this.imports("underscore");

    this.hasDefinition(function(_) {

        var StateDefinition = function(views, controllers) {

            this.views = views;
            this.resolve = {
                lazyLoadController : function($q, $rootScope) {

                    var deferred = $q.defer();
                    var imports = controllers;

                    require(imports, function() {
                        $rootScope.$apply(function() {
                            deferred.resolve();
                        });
                    });

                    return deferred.promise;
                }
            };
        };

        var VIEW_EXTENSION = ".html";
        var JS_EXTENSION = ".js";

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
            controllers : [ "/controller/addNoteController" ],
            parentStates : [ AppStates.READER_MAIN, AppStates.NOTEBOOK_MAIN ]
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
            controllers : [ "/controller/loginController" ],
            parentStates : [ AppStates.READER_MAIN, AppStates.NOTEBOOK_MAIN ]
        }, {
            stateName : AppStates.SETTINGS,
            views : [ {
                viewName : Views.MODAL_DIALOG,
                viewUrl : "/settingsDialogView"
            } ],
            controllers : [ "/controller/settingsController" ],
            parentStates : [ AppStates.NOTEBOOK_MAIN ]
        }, {
            stateName : AppStates.ADD_NOTEBOOK,
            views : [ {
                viewName : Views.MODAL_DIALOG,
                viewUrl : "/addNotebookDialogView"
            } ],
            controllers : [ "/controller/addNotebookController" ],
            parentStates : [ AppStates.NOTEBOOK_MAIN ]
        } ];

        var additionalViews = [ "/pageMessagesView", "/globalMessagesView", "/abstractNoteDialogView",
                "/notebookHeaderView" ];
        var additionalWhitelistUrls = [ App.Properties.pagesRoot + "/login" ];

        var whitelist = [];

        var constructParentViewName = function(viewName) {

            return viewName + "@";
        };

        var constructViews = function(views, parentState) {

            var absoluteViews = {};
            var viewName;

            _.each(views, function(view) {
                viewName = _.isUndefined(view.viewName) ? Views.MAIN : view.viewName;
                if (!_.isUndefined(parentState)) {
                    viewName = constructParentViewName(viewName);
                }
                absoluteViews[viewName] = {
                    templateUrl : App.Properties.ngViewsRoot + view.viewUrl + VIEW_EXTENSION
                };
            });

            return absoluteViews;
        };

        var constructControllers = function(controllers) {

            return _.map(controllers, function(controller) {
                return App.Properties.javascriptRoot + controller + JS_EXTENSION;
            });
        };

        var constructStateDefinition = function(views, controllers, parentState) {

            views = views || [];
            controllers = controllers || [];

            var absoluteViews = constructViews(views, parentState);
            var absoluteControllers = constructControllers(controllers);

            return new StateDefinition(absoluteViews, absoluteControllers);
        };

        var addAdditionalViewsToWhitelist = function() {

            var additionalViewUrls = _.map(additionalViews, function(view) {
                return App.Properties.ngViewsRoot + view + VIEW_EXTENSION;
            });
            whitelist = _.union(whitelist, additionalViewUrls, additionalWhitelistUrls);
        };

        var addViewsToWhitelist = function(views) {

            _.each(views, function(view) {
                whitelist.push(App.Properties.ngViewsRoot + view.viewUrl + VIEW_EXTENSION);
            });
        };

        var constructNestedStateName = function() {

            var stateName = [];

            _.each(arguments, function(argument) {
                stateName.push(argument);
            });

            return stateName.join(".");
        };

        var registerState = function($stateProvider, routingEntry) {

            var stateDefinition;
            var stateName;

            if (_.isUndefined(routingEntry.parentStates)) {
                stateDefinition = constructStateDefinition(routingEntry.views, routingEntry.controllers);
                $stateProvider.state(routingEntry.stateName, stateDefinition);
            }
            else {
                _.each(routingEntry.parentStates, function(parentState) {
                    stateName = constructNestedStateName(parentState, routingEntry.stateName);
                    stateDefinition = constructStateDefinition(routingEntry.views, routingEntry.controllers,
                            parentState);
                    $stateProvider.state(stateName, stateDefinition);
                });
            }
        };

        var setupStates = function($stateProvider, $sceDelegateProvider) {

            _.each(routingTable, function(routingEntry) {
                addViewsToWhitelist(routingEntry.views);
                registerState($stateProvider, routingEntry);
            });

            addAdditionalViewsToWhitelist();
            $sceDelegateProvider.resourceUrlWhitelist(whitelist);
        };

        var setMainState = function(stateName) {

            AppStates.MAIN = stateName;
        };

        var goRelative = function($state, stateName) {

            $state.go("." + stateName);
        };

        return {
            setupStates : setupStates,
            setMainState : setMainState,
            goRelative : goRelative
        };
    });
});
