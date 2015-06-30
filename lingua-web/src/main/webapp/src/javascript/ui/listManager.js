define(
    [
        "jquery"
    ],
    function ($) {

        return function () {

            var MOVE_CURSOR = "move";
            var Y_AXIS = "y";

            var collection;

            function convertOneBasedIndexToZeroBased(value) {

                return value - 1;
            }

            function convertZeroBasedIndexToOneBased(value) {

                return value + 1;
            }

            /**
             *
             * @param newPosition Zero based
             */
            function updatePositions(instance, newPosition) {

                var oldPosition = instance.position;
                newPosition = convertZeroBasedIndexToOneBased(newPosition);

                _.each(collection, function (instance) {
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
            }

            /**
             * @param oldPosition One based
             */
            function revertPositions(instance, oldPosition) {

                oldPosition = convertOneBasedIndexToZeroBased(oldPosition);
                updatePositions(instance, oldPosition);
            }

            function setCollection(collection_p) {

                collection = collection_p;
            }

            function setup(config) {

                $(config.list).sortable({
                    axis: Y_AXIS,
                    containment: config.container,
                    cursor: MOVE_CURSOR,
                    handle: config.handle,
                    stop: config.dragHandler
                });
            }

            function doExport() {

                this.setCollection = setCollection;
                this.updatePositions = updatePositions;
                this.revertPositions = revertPositions;
                this.setup = setup;
            }

            doExport.call(this);
        }
    });
