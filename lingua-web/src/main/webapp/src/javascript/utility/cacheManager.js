define(
    [
        "module/ngModule",
        "underscore"
    ],
    function (ngModule, _) {

        ngModule.factory("cacheManager",

            function () {

                var caches = {};

                function checkCacheExists(cacheName) {

                    if (_.isUndefined(caches[cacheName])) {
                        throw new Error("No such cache");
                    }
                }

                function lookup(cacheName, key) {

                    checkCacheExists(cacheName);
                    return caches[cacheName][key];
                }

                function put(cacheName, key, value) {

                    checkCacheExists(cacheName);
                    caches[cacheName][key] = value;
                }

                function create(cacheName) {

                    if (!_.isUndefined(caches[cacheName])) {
                        throw new Error("Cache already exists");
                    }

                    caches[cacheName] = {};
                }

                return {
                    lookup: lookup,
                    put: put,
                    create: create
                };
            });
    });
