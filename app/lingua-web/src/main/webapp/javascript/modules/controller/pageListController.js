App.Controller.createNew(function() {

    this.isCalled("pageListController");

    this.imports("jquery");
    this.imports("util/sortableListHelper");

    this.loads("directive/linguaPage");

    this.injects("$scope");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");

    this.hasDefinition(function($, sortableListHelper) {

        var TABS_CONTAINER = "#tabs-pane .inner";
        var TABS_LIST = "#notebook-tabs";
        var TAB_HANDLE = ".handle";
        var Y_AXIS = "y";
        var MOVE_CURSOR = "move";

        var findPageByPageId = function($scope, pageId) {

            var searchCriteria = {
                pageId : pageId
            };
            var page = _.findWhere($scope.global.model.currentNotebook.pages, searchCriteria);

            return page;
        };

        var getPageModelFromPage = function(page) {

            var pageModel = new Page();
            _.each(pageModel, function(value, key) {
                pageModel[key] = page[key];
            });

            return pageModel;
        };

        var getDragStopHandler = function($scope, notebookService, messageHandler) {

            return function(event, ui) {

                var pageId = $(ui.item).data().pageid;
                var newPosition = ui.item.index();

                updatePagePosition($scope, notebookService, messageHandler, pageId, newPosition);
            };
        };

        var getUpdatePageSuccessHandler = function(page) {

            return function(updatedModel) {

                _.each(updatedModel, function(key) {
                    page[key] = updatedModel[key];
                });
            };
        };

        var getUpdatePageFailureHandler = function($scope, messageHandler) {

            return function(data, status, headers, config) {

                $scope.func.loadNotesIntoPage();
                messageHandler
                        .addFreshPageMessage($scope, LocalStrings.inlinePageRearrangeError, MessageSeverity.ERROR);
            };
        };

        var updatePage = function(page, $scope, notebookService, messageHandler) {

            var pageModel = getPageModelFromPage(page);
            var successHandler = getUpdatePageSuccessHandler(page);
            var failureHandler = getUpdatePageFailureHandler($scope, messageHandler);

            notebookService.updatePage(page.pageId, pageModel, successHandler, failureHandler);
        };

        var updatePagePosition = function($scope, notebookService, messageHandler, pageId, newPosition) {

            var page = findPageByPageId($scope, pageId);
            sortableListHelper.updateNotePositionsInInterim(page, $scope.global.model.currentNotebook.pages,
                    newPosition);
            updatePage(page, $scope, notebookService, messageHandler);
        };

        var makeListSortable = function($scope, notebookService, messageHandler) {

            var dragStopHandler = getDragStopHandler($scope, notebookService, messageHandler);

            $(TABS_LIST).sortable({
                axis : Y_AXIS,
                containment : TABS_CONTAINER,
                cursor : MOVE_CURSOR,
                handle : TAB_HANDLE,
                stop : dragStopHandler
            });
        };

        return function($scope, notebookService, messageHandler) {

            makeListSortable($scope, notebookService, messageHandler);
        };
    });
});
