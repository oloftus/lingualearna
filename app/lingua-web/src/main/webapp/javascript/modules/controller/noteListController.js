App.Controller.createNew(function() {

    this.isCalled("noteListController");
    
    this.imports("jquery");
    
    this.loads("directive/linguaNote");
    
    this.injects("$scope");
    
    this.hasDefinition(function($) {

        return function($scope) {

            $("#notes-list").sortable({
                axis : "y",
                containment : "parent",
                cursor : "move",
                handle : ".handle",
                stop : function(event, ui) {
                    console.log(event, ui);
                }
            });
        };
    });
});
