(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "underscore" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper, _) {

        var CommsPipe = function() {

            var subscribers = {};
            var anySubscribers = {};

            var send = function(senderName, receiverName, message) {

                if (!_.isUndefined(subscribers[receiverName])
                        && !_.isUndefined(subscribers[receiverName][senderName])) {
                    subscribers[receiverName][senderName](message);
                }
                if (!_.isUndefined(anySubscribers[receiverName])) {
                    _.each(anySubscribers[receiverName], function(subscriberCallback) {
                        subscriberCallback(message);
                    });
                }
            };

            var subscribe = function(senderName, receiverName, subscriberCallback) {

                if (senderName === Components.ANY) {
                    if (_.isUndefined(anySubscribers[receiverName])) {
                        anySubscribers[receiverName] = [];
                    }
                    anySubscribers[receiverName].push(subscriberCallback);
                }
                else {
                    if (_.isUndefined(subscribers[receiverName])) {
                        subscribers[receiverName] = {};
                    }
                    subscribers[receiverName][senderName] = subscriberCallback;
                }
            };

            return {
                send : send,
                subscribe : subscribe
            };
        };

        ngRegistrationHelper(linguaApp).registerFactory("commsPipe", [ CommsPipe ]);
    });
})();
