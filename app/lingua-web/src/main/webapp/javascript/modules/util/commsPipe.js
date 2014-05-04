(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "underscore" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper, _) {

        var CommsPipe = function() {

            var subscribers = {};
            var subscribersToAnySender = {};

            var send = function(senderName, receiverName, message) {

                if (!_.isUndefined(subscribers[receiverName])
                        && !_.isUndefined(subscribers[receiverName][senderName])) {
                    subscribers[receiverName][senderName](message);
                }
                if (!_.isUndefined(subscribersToAnySender[receiverName])) {
                    subscribersToAnySender[receiverName](message);
                }
            };

            var subscribe = function(senderName, receiverName, subscriberCallback) {

                if (senderName === Components.ANY) {
                    subscribersToAnySender[receiverName] = subscriberCallback;
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
