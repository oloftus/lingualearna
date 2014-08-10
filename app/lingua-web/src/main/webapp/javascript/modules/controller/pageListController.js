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

        var getUpdatePageFailureHandler = function($scope, messageHandler, page, oldPosition) {

            return function(data, status, headers, config) {

                sortableListHelper.rejigPositions(page, $scope.global.model.currentNotebook.pages, oldPosition);
                messageHandler
                        .addFreshPageMessage($scope, LocalStrings.inlinePageRearrangeError, MessageSeverity.ERROR);
            };
        };

        var updatePage = function(page, $scope, notebookService, failureHandler) {

            var pageModel = getPageModelFromPage(page);
            var successHandler = getUpdatePageSuccessHandler(page);

            notebookService.updatePage(page.pageId, pageModel, successHandler, failureHandler);
        };

        var updatePagePosition = function($scope, notebookService, messageHandler, pageId, newPosition) {

            var page = findPageByPageId($scope, pageId);
            var oldPosition = sortableListHelper.convertOneBasedIndexToZeroBased(page.position);
            var failureHandler = getUpdatePageFailureHandler($scope, messageHandler, page, oldPosition);

            sortableListHelper.rejigPositions(page, $scope.global.model.currentNotebook.pages, newPosition);
            updatePage(page, $scope, notebookService, failureHandler);
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
        
        var setCurrentPageToClosestPage = function($scope, page) {
            
            if (!_.isEmpty($scope.global.model.currentNotebook.pages)) {
                var closestPages = _.filter($scope.global.model.currentNotebook.pages, function(iteratorPage) {
                    return iteratorPage.position < page.position;
                });
                
                var closestPagesAreBelow = _.isEmpty(closestPages);
                if (closestPagesAreBelow) {
                    closestPages = _.filter($scope.global.model.currentNotebook.pages, function(iteratorPage) {
                        return iteratorPage.position > page.position;
                    });
                }
                    
                var closestPage;
                if (closestPagesAreBelow) {
                    closestPage = _.min(closestPages, function(iteratorPage) {
                        return iteratorPage.position;
                    });
                }
                else {
                    closestPage = _.max(closestPages, function(iteratorPage) {
                        return iteratorPage.position;
                    });
                }
                
                $scope.global.model.currentPage = closestPage;
            }
            else {
                $scope.global.model.currentPage = null;
            }
        };
        
        var setupClickHandlers = function($scope, notebookService, messageHandler) {

            $scope.func.doDeletePage = function(page) {

                var successHandler = function() {

                    var pages = $scope.global.model.currentNotebook.pages;
                    var pageIndex = _.indexOf(pages, page);
                    pages.splice(pageIndex, 1);
                    
                    if ($scope.global.model.currentPage == page) {
                        setCurrentPageToClosestPage($scope, page);
                    }
                };

                var failureHandler = function() {

                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlinePageDeleteError,
                            MessageSeverity.ERROR);
                };

                notebookService.removePage(page.pageId, successHandler, failureHandler);
            };
        };

        return function($scope, notebookService, messageHandler) {

            makeListSortable($scope, notebookService, messageHandler);
            setupClickHandlers($scope, notebookService, messageHandler);
        };
    });
});
