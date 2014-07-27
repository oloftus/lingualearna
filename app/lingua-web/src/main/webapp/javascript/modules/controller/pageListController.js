App.Controller.createNew(function() {

    this.isCalled("pageListController");

    this.imports("jquery");

    this.loads("directive/linguaPage");

    this.injects("$scope");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");

    this.hasDefinition(function($) {

        var TABS_CONTAINER = "#tabs-pane .inner";
        var TABS_LIST = "#notebook-tabs";
        var TAB_HANDLE = ".handle";
        
        var convertZeroBasedIndexToOneBased = function(value) {
            
            return value + 1;
        };
        
        var updatePagePosition = function($scope, notebookService, messageHandler, pageId, newPosition) {
            
            var searchCriteria = {
                pageId : pageId
            };
            var page = _.findWhere($scope.global.model.currentNotebook.pages, searchCriteria);
            
            var oldPosition = page.position;
            newPosition = convertZeroBasedIndexToOneBased(newPosition);
            
            _.each($scope.global.model.currentNotebook.pages, function(page) {
                if (oldPosition < newPosition) {
                    if (oldPosition <= page.position && page.position <= newPosition) {
                        page.position--;
                    }
                }
                else {
                    if (newPosition <= page.position && page.position < oldPosition) {
                        page.position++;
                    }
                }
            });

            page.position = newPosition;
            
            var failureHandler = function(data, status, headers, config) {
                
                $scope.func.loadNotesIntoPage(); // TODO: Find page equivalent
                messageHandler.addFreshPageMessage($scope, LocalStrings.inlinePageRearrangeError, MessageSeverity.ERROR);
            };
            
            updatePage(page, $scope, notebookService, failureHandler);
        };

        var updatePage = function(page, $scope, notebookService, failureHandler) {

            var pageModel = new Page();
            for (var key in pageModel) {
                pageModel[key] = page[key];
            }
            
            var successHandler = function(updatedModel) {

                for (var key in updatedModel) {
                    page[key] = updatedModel[key];
                }
            };
            
            notebookService.updatePage(pageModel.pageId, pageModel, successHandler, failureHandler);
        };
        
        var makeListSortable = function($scope, notebookService, messageHandler) {

            $(TABS_LIST).sortable({
                axis : "y",
                containment : TABS_CONTAINER,
                cursor : "move",
                handle : TAB_HANDLE,
                stop : function(event, ui) {
                    updatePagePosition($scope, notebookService, messageHandler, $(ui.item).data().pageid, ui.item.index());
                }
            });
        };


        return function($scope, notebookService, messageHandler) {

            makeListSortable($scope, notebookService, messageHandler);
        };
    });
});
