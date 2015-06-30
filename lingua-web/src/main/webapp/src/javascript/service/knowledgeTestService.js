define(
    [
        "module/ngModule",
        "types/enums",
        "service/jsonWebService",
        "utility/cacheManager"
    ],
    function (ngModule, enums) {

        ngModule.service("knowledgeTestService",
            [
                "jsonWebService",
                "cacheManager",
                "knowledgeTestRandomEntriesUrl",
                "knowledgeTestSourceContextUrl",

                function (jsonWebService, cacheManager, knowledgeTestRandomEntriesUrl, knowledgeTestSourceContextUrl) {

                    function getRandomTestEntries(notebookId, successCallback, failureCallback) {

                        var serviceUrlWithParams = knowledgeTestRandomEntriesUrl.replace("{notebookId}", notebookId);
                        jsonWebService.execute(serviceUrlWithParams, enums.HttpMethod.GET, null, successCallback,
                            failureCallback, true);
                    }

                    function getSourceContext(noteId, successCallback, failureCallback) {

                        var cachedSourceContext = cacheManager.lookup(enums.Caches.SOURCE_CONTEXTS, noteId);

                        if (cachedSourceContext) {
                            successCallback(cachedSourceContext);
                            return;
                        }

                        function cacheResultAndCallSuccessCallback(sourceContext) {

                            cacheManager.put(enums.Caches.SOURCE_CONTEXTS, noteId, sourceContext);
                            successCallback(sourceContext);
                        }

                        var serviceUrlWithParams = knowledgeTestSourceContextUrl.replace("{noteId}", noteId);
                        jsonWebService.execute(serviceUrlWithParams, enums.HttpMethod.GET, null,
                            cacheResultAndCallSuccessCallback, failureCallback, true);
                    }

                    function construct() {

                        cacheManager.create(enums.Caches.SOURCE_CONTEXTS);
                    }

                    construct();

                    return {
                        getRandomTestEntries: getRandomTestEntries,
                        getSourceContext: getSourceContext
                    };
                }
            ])
    });
