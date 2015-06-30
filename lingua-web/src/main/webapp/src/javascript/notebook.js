define(
    [
        "module/ngModule",
        "config/properties",
        "module/abstractModule",
        "controller/module/notebookController"
    ],
    function (ngModule, properties, abstractModule) {

        ngModule.constant("noteServiceUrl", properties.noteServiceUrl);
        ngModule.constant("languageNamesServiceUrl", properties.languageNamesServiceUrl);
        ngModule.constant("supportedLanguagesServiceUrl", properties.supportedLanguagesServiceUrl);
        ngModule.constant("translateServiceUrl", properties.translateServiceUrl);
        ngModule.constant("notesPagesServiceUrl", properties.notesPagesServiceUrl);
        ngModule.constant("notebookServiceUrl", properties.notebookServiceUrl);
        ngModule.constant("notesByPageServiceUrl", properties.notesByPageServiceUrl);
        ngModule.constant("pageServiceUrl", properties.pageServiceUrl);
        ngModule.constant("knowledgeTestRandomEntriesUrl", properties.knowledgeTestRandomEntriesUrl);
        ngModule.constant("knowledgeTestSourceContextUrl", properties.knowledgeTestSourceContextUrl);

        abstractModule.bootstrap();
    });
