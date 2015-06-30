define(
    [
        "module/ngModule",
        "types/types",
        "types/enums",
        "localization/strings",
        "ui/listManager",
        "jquery",
        "directive/lgPage",
        "service/pageService",
        "ui/notificationManager",
        "utility/messageBus",
        "controller/abstractController",
        "service/pageService"
    ],
    function (ngModule, types, enums, strings, ListManager, $) {

        var PAGE_RENAME_ERROR_MESSAGE = "Pages can't have names that long";
        var ESC_KEY = 27;
        var ENTER_KEY = 13;
        var PAGE_ID_PARAM = "%pageId";
        var TABS_CONTAINER = "#lingua-binder-pages-pane .lingua-inner";
        var TABS_LIST = "#lingua-binder-pages ul";
        var TAB_HANDLE = ".lingua-handle";
        var TAB_CONTENT_SELECTOR = "#lingua-notebook-page-%pageId .lingua-tab span";

        ngModule.controller("pageListController",
            [
                "$scope",
                "$timeout",
                "pageService",
                "notificationManager",
                "messageBus",
                "abstractController",
                "pageService",

                function ($scope, $timeout, pageService, NotificationHandler, messageBus, AbstractController,
                          pageService) {

                    var notificationManager = new NotificationHandler($scope);
                    var listManager = new ListManager();
                    var abstractController = new AbstractController($scope);

                    function findPageByPageId($scope, pageId) {

                        var searchCriteria = {
                            pageId: pageId
                        };
                        return _.findWhere($scope.global.model.currentNotebook.pages, searchCriteria);
                    }

                    function getPageModelFromPage(page) {

                        var pageModel = new types.Page();
                        _.each(pageModel, function (value, key) {
                            pageModel[key] = page[key];
                        });

                        return pageModel;
                    }

                    function getDragStopHandler() {

                        return function (event, ui) {

                            var pageId = $(ui.item).data().pageid;
                            var newPosition = ui.item.index();

                            updatePagePosition(pageId, newPosition);
                        };
                    }

                    function getUpdatePageSuccessHandler(page) {

                        return function (updatedModel) {

                            _.each(updatedModel, function (key) {
                                page[key] = updatedModel[key];
                            });
                        };
                    }

                    function getUpdatePageFailureHandler(page, oldPosition) {

                        return function () {

                            listManager.revertPositions(page, oldPosition);
                            notificationManager
                                .newPageMessage(strings.inlinePageRearrangeError,
                                enums.MessageSeverity.ERROR);
                        };
                    }

                    function updatePage(page, failureHandler) {

                        var pageModel = getPageModelFromPage(page);
                        var successHandler = getUpdatePageSuccessHandler(page);

                        pageService.updatePage(page.pageId, pageModel, successHandler, failureHandler);
                    }

                    function updatePagePosition(pageId, newPosition) {

                        var page = findPageByPageId($scope, pageId);
                        var oldPosition = page.position;
                        var failureHandler = getUpdatePageFailureHandler(page, oldPosition);

                        listManager.setCollection($scope.global.model.currentNotebook.pages);
                        listManager.updatePositions(page, newPosition);
                        updatePage(page, failureHandler);
                    }

                    function setCurrentPageToClosestPage(page) {

                        if (!_.isEmpty($scope.global.model.currentNotebook.pages)) {
                            var closestPages = _.filter($scope.global.model.currentNotebook.pages,
                                function (iteratorPage) {
                                    return iteratorPage.position < page.position;
                                });

                            var closestPagesAreBelow = _.isEmpty(closestPages);
                            if (closestPagesAreBelow) {
                                closestPages = _.filter($scope.global.model.currentNotebook.pages,
                                    function (iteratorPage) {
                                        return iteratorPage.position > page.position;
                                    });
                            }

                            var closestPage;
                            if (closestPagesAreBelow) {
                                closestPage = _.min(closestPages, function (iteratorPage) {
                                    return iteratorPage.position;
                                });
                            }
                            else {
                                closestPage = _.max(closestPages, function (iteratorPage) {
                                    return iteratorPage.position;
                                });
                            }

                            $scope.global.model.currentPage = closestPage;
                        }
                        else {
                            $scope.global.model.currentPage = null;
                        }
                    }

                    function deletePage(page) {

                        function successHandler() {

                            var pages = $scope.global.model.currentNotebook.pages;
                            var pageIndex = _.indexOf(pages, page);
                            pages.splice(pageIndex, 1);

                            if ($scope.global.model.currentPage == page) {
                                setCurrentPageToClosestPage(page);
                            }
                        }

                        function failureHandler() {

                            notificationManager.newPageMessage(strings.inlinePageDeleteError,
                                enums.MessageSeverity.ERROR);
                        }

                        pageService.removePage(page.pageId, successHandler, failureHandler);
                    }

                    function changeCurrentPage(page) {

                        var searchCriteria = {
                            pageId: page.pageId
                        };

                        $scope.global.model.currentPage = _.findWhere($scope.global.model.currentNotebook.pages,
                            searchCriteria);

                        messageBus.send(enums.Components.BINDER, enums.Components.ANY,
                            enums.Signals.CURRENT_PAGE_CHANGED);
                    }

                    function deactivateRenamePageMode($tabContent, oldPageName) {

                        $tabContent.attr("contenteditable", "false");
                        $tabContent.off("keydown");

                        if (oldPageName) {
                            $tabContent.html(oldPageName);
                        }
                    }

                    function saveRenamedPage($tabContent, page, oldPageName) {

                        var pageModel = new types.Page($tabContent.text(), page.notebookId, page.pageId, page.position);

                        function successHandler(updatedPage) {
                            page.name = updatedPage.name;
                        }

                        function errorHandler() {

                            $tabContent.text(oldPageName);
                            notificationManager.newPageMessage(PAGE_RENAME_ERROR_MESSAGE, enums.MessageSeverity.ERROR);
                        }

                        pageService.updatePage(page.pageId, pageModel, successHandler, errorHandler);
                    }

                    function activateRenamePageMode(page) {

                        var oldPageName = page.name;
                        var $tabContent = $(TAB_CONTENT_SELECTOR.replace(PAGE_ID_PARAM, page.pageId));

                        $tabContent.attr("contenteditable", "true");
                        $tabContent.selectText();

                        $tabContent.keydown(function (evt) {

                            switch (evt.keyCode) {
                                case ESC_KEY:
                                    deactivateRenamePageMode($tabContent, oldPageName);
                                    break;

                                case ENTER_KEY:
                                    saveRenamedPage($tabContent, page, oldPageName);
                                    deactivateRenamePageMode($tabContent);
                                    break;
                            }
                        });

                        $tabContent.blur(function (evt) {
                            deactivateRenamePageMode($tabContent, oldPageName);
                        })
                    }

                    function setupClickHandlers() {

                        $scope.func.changeCurrentPage = changeCurrentPage;
                        $scope.func.doDeletePage = deletePage;
                        $scope.func.activateRenamePageMode = activateRenamePageMode;
                    }

                    function setupPageList() {

                        if ($(TABS_LIST).length > 0) {
                            listManager.setup({
                                list: TABS_LIST,
                                container: TABS_CONTAINER,
                                handle: TAB_HANDLE,
                                dragHandler: getDragStopHandler()
                            });
                        }
                        else {
                            $timeout(setupPageList, 10);
                        }
                    }

                    function construct() {

                        abstractController.setup();
                        setupClickHandlers();
                        setupPageList();
                    }

                    construct();
                }
            ])
    });
