(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var NotebookService = function(jsonWebService, notesPagesServiceUrl) {
            
            var removePagesThenCallback = function(data, callback) {

                _.each(data, function(notebook) {
                    delete notebook.pages;
                });
                
                callback(data);
            };
            
            var getListOfNotebooks = function(successCallback, failureCallback) {
                
                jsonWebService.execute(notesPagesServiceUrl, HttpMethod.GET, null, function(data) {
                    removePagesThenCallback(data, successCallback);
                }, failureCallback, true);
            };
            
            return {
                getListOfNotebooks : getListOfNotebooks
            };
        };

        ngRegistrationHelper(linguaApp).registerService("notebookService",
                [ "jsonWebService", "notesPagesServiceUrl", NotebookService ]);
    });
})();
