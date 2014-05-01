(function(){
    
    var dependencies = [ "linguaApp" ];
    
    define(dependencies, function(linguaApp) {
        
        var getSelected = function() {

            var selected = "";
            if (window.getSelection) {
                selected = window.getSelection();
            }
            else if (document.getSelection) {
                selected = document.getSelection();
            }
            else if (document.selection) {
                selected = document.selection.createRange().text;
            }
            return selected;
        };

        var clearSelected = function() {

            if (window.getSelection().empty) {
                window.getSelection().empty();
            }
            else if (window.getSelection().removeAllRanges) {
                window.getSelection().removeAllRanges();
            }
            else if (document.selection) {
                document.selection.empty();
            }
        };
        
        return {
            getSelected : getSelected,
            clearSelected : clearSelected,
        };
    });
})();
