define(
    [
        "underscore",
        "underscore.string",
        "jquery"
    ],
    function (_, str, $) {

        function setupUnderscoreOverrides() {

            _.mixin(str.exports());
        }

        function setupJqueryOverrides() {

            String.prototype.endsWith = function (searchStr) {
                return this.indexOf(searchStr, this.length - searchStr.length) !== -1;
            };

            String.prototype.startsWith = function (searchStr) {
                return this.indexOf(searchStr) === 0;
            };

            $.fn.selectText = function () {

                var doc = document;
                var element = this[0];

                if (doc.body.createTextRange) {
                    var range = document.body.createTextRange();
                    range.moveToElementText(element);
                    range.select();
                }
                else if (window.getSelection) {
                    var selection = window.getSelection();
                    var range = document.createRange();
                    range.selectNodeContents(element);
                    selection.removeAllRanges();
                    selection.addRange(range);
                }
            };
        }

        function setup() {

            setupUnderscoreOverrides();
            setupJqueryOverrides();
        }

        return {
            setup: setup
        }
    });
