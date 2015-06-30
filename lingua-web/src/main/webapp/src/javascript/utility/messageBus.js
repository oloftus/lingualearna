define(
    [
        "module/ngModule",
        "types/enums",
        "underscore"
    ],
    function (ngModule, enums, _) {

        ngModule.factory("messageBus",
            function () {

                var subscribersById = {};
                var subscribers = {};
                var subscribersToAnySender = {};

                function newUniqueRandomId(subscribers) {

                    var subscriberId;
                    do {
                        subscriberId = Math.random().toString().substring(2);
                    } while (subscribers[subscriberId]);

                    return subscriberId;
                }

                function Subscriber(subscribers, senderName, receiverName, callback, subjectAcceptor, messageAcceptor) {

                    this.subscriberId = newUniqueRandomId(subscribers);
                    this.senderName = senderName;
                    this.receiverName = receiverName;
                    this.callback = callback;
                    this.subjectAcceptor = subjectAcceptor;
                    this.messageAcceptor = messageAcceptor;
                }

                function sendIfAcceptorMatches(subscriber, message, subject) {

                    var subjectAcceptorMatches = _.isNull(subscriber.subjectAcceptor) ||
                        !_.isNull(subscriber.subjectAcceptor) && subscriber.subjectAcceptor(subject);
                    var messageAcceptorMatches = _.isNull(subscriber.messageAcceptor) ||
                        !_.isNull(subscriber.messageAcceptor) && subscriber.messageAcceptor(message);

                    if (subjectAcceptorMatches && messageAcceptorMatches) {
                        subscriber.callback(message, subject);
                    }
                }

                function sendToSubscribers(subscribers, message, subject) {

                    _.each(subscribers, function (subscriber) {
                        sendIfAcceptorMatches(subscriber, message, subject);
                    });
                }

                function acceptorFromAcceptorOrSignal(acceptorOrSignal) {

                    var acceptor = null;

                    if (_.isUndefined(acceptorOrSignal) || _.isNull(acceptorOrSignal)) {
                        return acceptor;
                    }
                    else if (_.isFunction(acceptorOrSignal)) {
                        acceptor = acceptorOrSignal;
                    }
                    else {
                        var signal = acceptorOrSignal;
                        acceptor = function (value) {
                            return value === signal;
                        };
                    }

                    return acceptor;
                }

                function send(senderName, receiverName, subject, message) {

                    if (!_.isUndefined(subscribers[receiverName]) && !_.isUndefined(subscribers[receiverName][senderName])) {
                        sendToSubscribers(subscribers[receiverName][senderName], message, subject);
                    }

                    if (!_.isUndefined(subscribersToAnySender[receiverName])) {
                        sendToSubscribers(subscribersToAnySender[receiverName], message, subject);
                    }
                }

                function subscribe(senderName, receiverName, callback, subjectAcceptor, messageAcceptor) {

                    subjectAcceptor = acceptorFromAcceptorOrSignal(subjectAcceptor);
                    messageAcceptor = acceptorFromAcceptorOrSignal(messageAcceptor);

                    var subscriber = new Subscriber(subscribers, senderName, receiverName, callback, subjectAcceptor,
                        messageAcceptor);

                    subscribersById[subscriber.subscriberId] = subscriber;

                    if (senderName === enums.Components.ANY) {
                        if (_.isUndefined(subscribersToAnySender[receiverName])) {
                            subscribersToAnySender[receiverName] = [];
                        }
                        subscribersToAnySender[receiverName].push(subscriber);
                    }
                    else {
                        if (_.isUndefined(subscribers[receiverName])) {
                            subscribers[receiverName] = {};
                        }
                        if (_.isUndefined(subscribers[receiverName][senderName])) {
                            subscribers[receiverName][senderName] = [];
                        }
                        subscribers[receiverName][senderName].push(subscriber);
                    }

                    return subscriber.subscriberId;
                }

                function unsubscribe(subscriberId) {

                    var subscriber = subscribersById[subscriberId];
                    delete subscribersById[subscriberId];

                    if (subscriber.senderName === enums.Components.ANY) {
                        delete subscribersToAnySender[subscriber.receiverName];
                    }
                    else {
                        delete subscribers[subscriber.receiverName][subscriber.senderName];
                    }
                }

                return {
                    send: send,
                    subscribe: subscribe,
                    unsubscribe: unsubscribe
                };
            });
    });
