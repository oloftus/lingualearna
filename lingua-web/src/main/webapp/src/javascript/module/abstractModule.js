define(
    [
        "module/ngModule",
        "angular",
        "utility/jsOverrides",
        "state/stateManager",
        "underscore"
    ],
    function (ngModule, angular, jsOverrides, stateManager) {

        function configureAngular() {

            function Configuration($stateProvider, $sceDelegateProvider, $httpProvider) {

                ngModule.httpProvider = $httpProvider;
                stateManager.setupStates($stateProvider, $sceDelegateProvider);
            }

            ngModule.config([ "$stateProvider", "$sceDelegateProvider", "$httpProvider", Configuration ]);
        }

        function bootstrapAngular() {

            angular.element(document).ready(function () {
                angular.bootstrap(document, [ "lingua-web" ]);
            });
        }

        function jqueryNoConflictAndRestore() {

            jQuery.noConflict(true);
            if (lingua.OldJquery) {
                $ = jQuery = lingua.OldJquery;
            }
        }

        function bootstrap() {

            jsOverrides.setup();
            configureAngular();
            bootstrapAngular();
            jqueryNoConflictAndRestore();
        }

        return {
            bootstrap: bootstrap
        };
    });
