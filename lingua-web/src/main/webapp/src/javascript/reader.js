define(
    [
        "module/ngModule",
        "config/properties",
        "module/abstractModule",
        "controller/module/readerController"
    ],
    function (ngModule, properties, abstractModule) {

        properties.csrfSecret = lingua.CsrfSecret;

        ngModule.constant("noteServiceUrl", properties.noteServiceUrl);
        ngModule.constant("languageNamesServiceUrl", properties.languageNamesServiceUrl);
        ngModule.constant("supportedLanguagesServiceUrl", properties.supportedLanguagesServiceUrl);
        ngModule.constant("translateServiceUrl", properties.translateServiceUrl);
        ngModule.constant("notesPagesServiceUrl", properties.notesPagesServiceUrl);
        ngModule.constant("notebookServiceUrl", properties.notebookServiceUrl);
        ngModule.constant("notesByPageServiceUrl", properties.notesByPageServiceUrl);
        ngModule.constant("pageServiceUrl", properties.pageServiceUrl);

        abstractModule.bootstrap();
    });
