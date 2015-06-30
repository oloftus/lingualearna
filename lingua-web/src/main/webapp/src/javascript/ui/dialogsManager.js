define(
    [
        "types/enums",
        "jquery"
    ],
    function (enums, $) {

        var DIALOG_CONFIGURATIONS = [
            {
                elem: "#lingua-main-dialog",
                topOffset: "130px",
                draggable: true,
                centre: true
            },
            {
                elem: "#lingua-knowledge-test-hint",
                draggable: false,
                centre: false
            }
        ];

        var DIALOG_HEADER_ELEM = ".lingua-dialog-header";

        return function ($scope) {

            function setup() {

                $scope.$on("$viewContentLoaded", function () {

                    _.each(DIALOG_CONFIGURATIONS, function (dialogConfig) {

                        var $dialog = $(dialogConfig.elem);

                        if (dialogConfig.draggable) {
                            $dialog.draggable({
                                handle: DIALOG_HEADER_ELEM,
                                containment: "document",
                                scroll: false
                            });
                        }

                        if (dialogConfig.centre) {
                            var $window = $(window);
                            var leftOffset = (($window.width() - $dialog.outerWidth()) / 2) + $window.scrollLeft();

                            $dialog.css({
                                "left": leftOffset + "px"
                            });
                        }

                        if (dialogConfig.topOffset) {
                            $dialog.css({
                                "top": dialogConfig.topOffset
                            });
                        }
                    });
                });
            }

            function doExport() {

                this.setup = setup;
            }

            doExport.call(this);
        }
    });
