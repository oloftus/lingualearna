App.Controller.createNew(function() {

    this.isCalled("noteListController");

    this.imports("jquery");

    this.loads("directive/linguaPage");

    this.injects("$scope");

    this.hasDefinition(function($) {

        var TABS_CONTAINER = "#tabs-pane .inner";
        var TABS_LIST = "#notebook-tabs";
        var TAB_HANDLE = ".handle";
        
        var makeListSortable = function($scope, noteService, messageHandler) {

            $(TABS_LIST).sortable({
                axis : "y",
                containment : TABS_CONTAINER,
                cursor : "move",
                handle : TAB_HANDLE,
                stop : function(event, ui) {
//                    updateNotePosition($scope, noteService, messageHandler, $(ui.item).data().noteid, ui.item.index());
                }
            });
        };


        return function($scope, noteService, messageHandler) {

            makeListSortable($scope);
        };
    });
});
