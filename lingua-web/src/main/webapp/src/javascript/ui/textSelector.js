define(
    [
        "underscore",
        "jquery"
    ],
    function (_, $) {

        var SELECTION_BOUND_LENGTH = 50;

        function cutOnWordBoundary(string) {

            return string.match(/^\S*\s*([\s\S]*?)\s*\S*$/)[1];
        }

        function getPrevsText($elem) {

            var $parent = $elem.parent();
            var prevsText = $parent.prevAll().text();

            if (prevsText.length < SELECTION_BOUND_LENGTH) {
                return getPrevsText($parent);
            }
            else {
                return prevsText;
            }
        }

        function getNextsText($elem) {

            var $parent = $elem.parent();
            var nextsText = $parent.nextAll().text();

            if (nextsText.length < SELECTION_BOUND_LENGTH) {
                return getNextsText($parent);
            }
            else {
                return nextsText;
            }
        }

        function getSourceContext(selected) {

            var selectedText = selected.toString();

            var $anchor = $(selected.anchorNode);
            var anchorImmediateContext = $anchor.text().substring(0, selected.anchorOffset);
            var anchorDistantContext = getPrevsText($anchor);
            var anchorContext = anchorDistantContext + anchorImmediateContext;
            var anchorContextOffset = Math.max(anchorContext.length - SELECTION_BOUND_LENGTH, 0);
            var anchorContextTrimmed = anchorContext.substring(anchorContextOffset);

            var $focus = $(selected.focusNode);
            var focusImmediateContext = $focus.text().substring(selected.focusOffset);
            var focusDistantContext = getNextsText($focus);
            var focusContext = focusImmediateContext + focusDistantContext;
            var focusContextOffset = Math.min(SELECTION_BOUND_LENGTH, focusContext.length);
            var focusContextTrimmed = focusContext.substring(0, focusContextOffset);

            return  anchorContextTrimmed + selectedText + focusContextTrimmed;
        }

        function getSelected() {

            var selected = window.getSelection();
            var selectedText = selected.toString();
            var sourceContext = "";

            if (!_.isEmpty(selectedText)) {
                sourceContext = cutOnWordBoundary(getSourceContext(selected));
            }

            return {
                selectedText: selectedText,
                contextText: sourceContext
            }
        }

        function clearSelected() {

            window.getSelection().removeAllRanges();
        }

        return {
            getSelected: getSelected,
            clearSelected: clearSelected
        };
    });
