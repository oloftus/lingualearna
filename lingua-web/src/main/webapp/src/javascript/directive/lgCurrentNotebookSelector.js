define(
    [
        "module/ngModule",
        "config/properties",
        "jquery"
    ],
    function (ngModule, properties, $) {

        var SELECTOR_ARROW_WIDTH = 40;
        var VIEW_EXTENSION = ".html";

        ngModule.directive("lgCurrentNotebookSelector",
            function () {

                function link(scope, element) {

                    /*
                     * Without these 'unnecessary' jQuery wrappers, there are timing issues
                     * in jQLite which leaves .change(...) & .width(...) undefined
                     */
                    var $selectBox = $(element.find("select"));
                    var $widthStore = $(element.find("span"));

                    function adjustWidth() {

                        var selectedText = $selectBox.find("option:selected").text();
                        $widthStore.html(selectedText);
                        $selectBox.width($widthStore.width() + SELECTOR_ARROW_WIDTH);
                    }

                    scope.$watchCollection("model.notebooks", adjustWidth);
                    $selectBox.change(adjustWidth);
                }

                return {
                    restrict: "E", // Match only element names
                    scope: {
                        model: "=",
                        func: "="
                    },
                    replace: true,
                    templateUrl: properties.templatesRoot + "/directive/lgCurrentNotebookSelector" + VIEW_EXTENSION,
                    link: link
                };
            });
    });
