App.Module.createNew(function() {

    this.isCalled("abstractListController");

    this.hasDefinition(function() {

        var convertZeroBasedIndexToOneBased = function(value) {
            
            return value + 1;
        };
        
        var updateNotePositionsInInterim = function(instance, collection, newPosition) {
            
            var oldPosition = instance.position;
            newPosition = convertZeroBasedIndexToOneBased(newPosition);
            
            _.each(collection, function(instance) {
                if (oldPosition < newPosition) {
                    if (oldPosition <= instance.position && instance.position <= newPosition) {
                        instance.position--;
                    }
                }
                else {
                    if (newPosition <= instance.position && instance.position < oldPosition) {
                        instance.position++;
                    }
                }
            });

            instance.position = newPosition;
        };

        return {
            updateNotePositionsInInterim : updateNotePositionsInInterim
        };
    });
});
