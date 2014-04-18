define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    // TODO: Extract superclass
    var NoteService = function(jsonWebService, noteServiceUrl) {
        
    	var create = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(noteServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                    failureCallback);

        };
        
        var retrieve = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(noteServiceUrl, HttpMethod.GET, translationRequest, successCallback,
                    failureCallback);

        };
        
        var update = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(noteServiceUrl, HttpMethod.PUT, translationRequest, successCallback,
                    failureCallback);

        };
        
        var remove = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(noteServiceUrl, HttpMethod.DELETE, translationRequest, successCallback,
                    failureCallback);

        };

        return {
            create : create,
            retrieve : retrieve,
            update : update,
            remove : remove
        };
    };

    linguaApp.service("noteService", [ "jsonWebService", "noteServiceUrl", NoteService ]);
});
