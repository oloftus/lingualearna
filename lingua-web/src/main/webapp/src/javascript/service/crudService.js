define(
    [
        "types/enums"
    ],
    function (enums) {

    /*
     * The intention to use a reference to CrudService later and let Angular
     * do the DI
     */
    return function (jsonWebService, serviceUrl) {

        function constructInstanceUrl(baseUrl, instanceId) {

            return baseUrl + "/" + instanceId;
        }

        function create(payload, successCallback, failureCallback) {

            jsonWebService.execute(serviceUrl, enums.HttpMethod.POST, payload, successCallback, failureCallback);
        }

        function retrieve(id, successCallback, failureCallback) {

            jsonWebService.execute(constructInstanceUrl(serviceUrl, id), enums.HttpMethod.GET, null, successCallback,
                failureCallback);
        }

        function update(id, payload, successCallback, failureCallback) {

            jsonWebService.execute(constructInstanceUrl(serviceUrl, id), enums.HttpMethod.PUT, payload, successCallback,
                failureCallback);
        }

        function remove(id, successCallback, failureCallback) {

            jsonWebService.execute(constructInstanceUrl(serviceUrl, id), enums.HttpMethod.DELETE, null, successCallback,
                failureCallback);
        }

        return {
            create: create,
            retrieve: retrieve,
            update: update,
            remove: remove
        };
    };
});
