define(function() {

    var properties = {};

    properties.applicationRoot = lingua.AppRoot;
    properties.csrfToken = lingua.CsrfToken;
    properties.pageMessagesTimeout = 4000; // Milliseconds
    properties.dialogDisappearTimeout = 4000; // Milliseconds

    properties.templatesRoot = properties.applicationRoot + "/templates";
    properties.resourcesRoot = properties.applicationRoot + "/resources";
    properties.apiRoot = properties.applicationRoot + "/app/api";

    properties.pagesRoot = properties.applicationRoot + "/app";
    properties.pingServiceUrl = properties.apiRoot + "/ping";
    properties.logoutUrl = properties.pagesRoot + "/logout";

    properties.translateServiceUrl = properties.apiRoot + "/translate";
    properties.csrfTokenApiUrl = properties.apiRoot + "/security/csrfToken";

    properties.noteServiceUrl = properties.apiRoot + "/note";
    properties.notesByPageServiceUrl = properties.apiRoot + "/page/{pageId}/notes";

    properties.languageServiceUrl = properties.apiRoot + "/languages";
    properties.languageNamesServiceUrl = properties.languageServiceUrl + "/langName";
    properties.supportedLanguagesServiceUrl = properties.languageServiceUrl + "/supported";

    properties.notebookServiceUrl = properties.apiRoot + "/notebook";
    properties.notesPagesServiceUrl = properties.notebookServiceUrl + "/notebooksPages";
    properties.pageServiceUrl = properties.notebookServiceUrl + "/page";

    properties.knowledgeTestRandomEntriesUrl = properties.apiRoot + "/knowledge/notebook/{notebookId}";
    properties.knowledgeTestSourceContextUrl = properties.apiRoot + "/knowledge/sourceContext/{noteId}";

    return properties;
});
