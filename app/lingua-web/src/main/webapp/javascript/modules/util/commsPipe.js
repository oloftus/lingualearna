App.Factory.createNew(function() {

    this.isCalled("commsPipe");

    this.imports("underscore");

    this.hasDefinition(function(_) {

        var newUniqueRandomId = function(subscribers) {
            
            var subscriberId;
            do {
                subscriberId = Math.random().toString().substring(2);
            } while (subscribers[subscriberId]);

            return subscriberId;
        };
        
        var Subscriber = function(subscribers, senderName, receiverName, callback, subjectAcceptor, messageAcceptor) {

            this.subscriberId = newUniqueRandomId(subscribers);
            this.senderName = senderName;
            this.receiverName = receiverName; 
            this.callback = callback;
            this.subjectAcceptor = subjectAcceptor;
            this.messageAcceptor = messageAcceptor;
        };
        
        var sendIfAcceptorMatches = function(subscriber, message, subject) {

            var subjectAcceptorMatches = _.isNull(subscriber.subjectAcceptor) ||
                    !_.isNull(subscriber.subjectAcceptor) && subscriber.subjectAcceptor(subject);
            var messageAcceptorMatches = _.isNull(subscriber.messageAcceptor) ||
                    !_.isNull(subscriber.messageAcceptor) && subscriber.messageAcceptor(message);

            if (subjectAcceptorMatches && messageAcceptorMatches) {
                subscriber.callback(message, subject);
            }
        };

        var sendToSubscribers = function(subscribers, message, subject) {

            _.each(subscribers, function(subscriber) {
                sendIfAcceptorMatches(subscriber, message, subject);
            });
        };

        var acceptorFromAcceptorOrSignal = function(acceptorOrSignal) {

            var acceptor = null;

            if (_.isUndefined(acceptorOrSignal) || _.isNull(acceptorOrSignal)) {
                return acceptor;
            }
            else if (_.isFunction(acceptorOrSignal)) {
                acceptor = acceptorOrSignal;
            }
            else {
                var signal = acceptorOrSignal;
                acceptor = function(value) {
                    return value === signal;
                };
            }

            return acceptor;
        };

        return function() {

            var subscribersById = {};
            var subscribers = {};
            var subscribersToAnySender = {};

            var send = function(senderName, receiverName, subject, message) {

                if (!_.isUndefined(subscribers[receiverName]) &&
                        !_.isUndefined(subscribers[receiverName][senderName])) {
                    sendToSubscribers(subscribers[receiverName][senderName], message, subject);
                }

                if (!_.isUndefined(subscribersToAnySender[receiverName])) {
                    sendToSubscribers(subscribersToAnySender[receiverName], message, subject);
                }
            };

            var subscribe = function(senderName, receiverName, callback, subjectAcceptor, messageAcceptor) {

                subjectAcceptor = acceptorFromAcceptorOrSignal(subjectAcceptor);
                messageAcceptor = acceptorFromAcceptorOrSignal(messageAcceptor);

                var subscriber = new Subscriber(subscribers, senderName, receiverName, callback, subjectAcceptor, messageAcceptor);

                subscribersById[subscriber.subscriberId] = subscriber;
                
                if (senderName === Components.ANY) {
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
            };
            
            var unsubscribe = function(subscriberId) {
                
                var subscriber = subscribersById[subscriberId];
                delete subscribersById[subscriberId];
                
                if (subscriber.senderName === Components.ANY) {
                    delete subscribersToAnySender[subscriber.receiverName];
                }
                else {
                    delete subscribers[subscriber.receiverName][subscriber.senderName];
                }
            };

            return {
                send : send,
                subscribe : subscribe,
                unsubscribe : unsubscribe,
            };
        };
    });
});
