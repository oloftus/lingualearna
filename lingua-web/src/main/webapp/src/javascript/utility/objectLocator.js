define(
    [
        "module/ngModule",
        "underscore"
    ],
    function (ngModule, _) {

        return function ($scope) {

            function findNoteByNoteId(noteId) {

                var searchCriteria = {
                    noteId: noteId
                };
                return _.findWhere($scope.global.model.currentPage.notes, searchCriteria);
            }

            function doExport() {

                this.findNoteByNoteId = findNoteByNoteId;
            }

            doExport.call(this);
        }
    });
