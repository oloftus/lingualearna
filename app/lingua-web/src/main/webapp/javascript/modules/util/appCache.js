App.Factory.createNew(function() {

    this.isCalled("appCache");

    this.imports("underscore");

    this.hasDefinition(function(_) {

        var caches = {};
        
        var checkCacheExists = function(cacheName) {
            
            if (_.isUndefined(caches[cacheName])) {
                throw new Error("No such cache");
            }
        };
        
        var lookup = function(cacheName, key) {

            checkCacheExists(cacheName);
            return caches[cacheName][key];
        };
        
        var put = function(cacheName, key, value) {
            
            checkCacheExists(cacheName);
            caches[cacheName][key] = value;
        };
        
        var create = function(cacheName) {
            
            if (!_.isUndefined(caches[cacheName])) {
                throw new Error("Cache already exists");
            }
            
            caches[cacheName] = {};
        };
        
        return function() {

            return {
                lookup : lookup,
                put : put,
                create : create
            };
        };
    });
});
