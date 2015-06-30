define(
    [
        "types/enums",
        "config/properties",
        "state/stateDefinitions",
        "underscore"
    ],
    function (enums, properties, stateDefinitions, _) {

        var ABSOLUTE_STATE_SEPARATOR = "@";

        function StateDefinition(views) {

            this.views = views;
        }

        var VIEW_EXTENSION = ".html";

        var whitelist = [];

        function constructParentViewName(viewName) {

            return viewName + ABSOLUTE_STATE_SEPARATOR;
        }

        function constructAbsoluteViewName(absoluteViewDefinition) {

            return absoluteViewDefinition.viewName + ABSOLUTE_STATE_SEPARATOR + absoluteViewDefinition.stateName;
        }

        function hasParentState(parentState) {

            return !_.isUndefined(parentState);
        }

        function isNormalView(view) {

            return !_.isUndefined(view.viewName);
        }

        function isAbsoluteView(view) {

            return !_.isUndefined(view.absoluteViewName);
        }

        function constructViews(views, parentState) {

            var absoluteViews = {};
            var viewName;

            _.each(views, function (view) {

                if (isNormalView(view)) {
                    viewName = view.viewName
                }
                else if (isAbsoluteView(view)) {
                    viewName = view.absoluteViewName;
                }
                else {
                    viewName = enums.Views.MAIN;
                }

                if (isAbsoluteView(view)) {
                    viewName = constructAbsoluteViewName(viewName);
                }
                else if (hasParentState(parentState)) {
                    viewName = constructParentViewName(viewName);
                }

                if (view.viewUrl) {
                    absoluteViews[viewName] = {
                        templateUrl: properties.templatesRoot + view.viewUrl + VIEW_EXTENSION
                    };
                }
                else {
                    absoluteViews[viewName] = {};
                }
            });

            return absoluteViews;
        }

        function constructStateDefinition(views, parentState) {

            views = views || [];

            var absoluteViews = constructViews(views, parentState);

            return new StateDefinition(absoluteViews);
        }

        function addAdditionalViewsToWhitelist() {

            var additionalViewUrls = _.map(stateDefinitions.additionalViews, function (view) {
                return properties.templatesRoot + view + VIEW_EXTENSION;
            });
            whitelist = _.union(whitelist, additionalViewUrls, stateDefinitions.additionalWhitelistUrls);
        }

        function addViewsToWhitelist(views) {

            _.each(views, function (view) {
                whitelist.push(properties.templatesRoot + view.viewUrl + VIEW_EXTENSION);
            });
        }

        function constructNestedStateName() {

            var stateName = [];

            _.each(arguments, function (argument) {
                stateName.push(argument);
            });

            return stateName.join(".");
        }

        function registerState($stateProvider, routingEntry) {

            var stateDefinition;
            var stateName;

            if (_.isUndefined(routingEntry.parentStates)) {
                stateDefinition = constructStateDefinition(routingEntry.views);
                $stateProvider.state(routingEntry.stateName, stateDefinition);
            }
            else {
                _.each(routingEntry.parentStates, function (parentState) {
                    stateName = constructNestedStateName(parentState, routingEntry.stateName);
                    stateDefinition = constructStateDefinition(routingEntry.views, parentState);
                    $stateProvider.state(stateName, stateDefinition);
                });
            }
        }

        function setupStates($stateProvider, $sceDelegateProvider) {

            _.each(stateDefinitions.routingTable, function (routingEntry) {
                addViewsToWhitelist(routingEntry.views);
                registerState($stateProvider, routingEntry);
            });

            addAdditionalViewsToWhitelist();
            $sceDelegateProvider.resourceUrlWhitelist(whitelist);
        }

        return {
            setupStates: setupStates
        };
    });
