(function() {

    var dependencies = [ "linguaApp", "underscore" ];

    /*
     * Really simple messaging system. A mailbox contains messages send
     * from a single sender to a single receiver. Messages are deleted
     * immediately when read. If a mailbox has no subscriber, sent messages are
     * queued and pushed when a subscriber is added. Subscribers are anonymous
     * functions. Messages are always sent in an array.
     */
    define(dependencies, function(linguaApp, _) {

        var InterAppMailbox = function() {

            var mailbox = {};
            var subscribers = {};

            var discard = function(senderName, receiverName) {

                delete mailbox[receiverName][senderName];
            };

            var subscriberExists = function(senderName, receiverName) {

                return !_.isUndefined(subscribers[receiverName])
                        && !_.isUndefined(subscribers[receiverName][senderName]);
            };

            var mailboxExists = function(senderName, receiverName) {

                return !_.isUndefined(mailbox[receiverName]) && !_.isEmpty(mailbox[receiverName][senderName]);
            };

            var createMailboxIfNotExists = function(senderName, receiverName) {

                if (_.isUndefined(mailbox[receiverName])) {
                    mailbox[receiverName] = {};
                }
                if (_.isUndefined(mailbox[receiverName][senderName])) {
                    mailbox[receiverName][senderName] = [];
                }
            };

            var queueMessage = function(senderName, receiverName, message) {

                mailbox[receiverName][senderName].push(message);
            };

            var pushMessageToSubscriber = function(senderName, receiverName, message) {

                subscribers[receiverName][senderName]([ message ]);
            };

            var addSubscriber = function(senderName, receiverName, subscriberCallback) {

                if (_.isUndefined(subscribers[receiverName])) {
                    subscribers[receiverName] = {};
                }
                subscribers[receiverName][senderName] = subscriberCallback;
            };

            var pushQueuedMessages = function(senderName, receiverName, subscriberCallback) {

                if (mailboxExists(senderName, receiverName)) {
                    subscriberCallback(mailbox[receiverName][senderName]);
                    discard(senderName, receiverName);
                }
            };

            var send = function(senderName, receiverName, message) {

                if (subscriberExists(senderName, receiverName)) {
                    pushMessageToSubscriber(senderName, receiverName, message);
                }
                else {
                    createMailboxIfNotExists(senderName, receiverName);
                    queueMessage(senderName, receiverName, message);
                }
            };

            var subscribe = function(senderName, receiverName, subscriberCallback) {

                addSubscriber(senderName, receiverName, subscriberCallback);
                pushQueuedMessages(senderName, receiverName, subscriberCallback);
            };

            return {
                send : send,
                subscribe : subscribe
            };
        };

        linguaApp.provide.factory("interAppMailbox", [ InterAppMailbox ]);
    });
})();
