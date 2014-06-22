App.Factory.createNew(function() {

    this.isCalled("commsPipe");
    
    this.imports("underscore");

    this.hasDefinition(function(_) {

        var Subscriber = function(callback, subjectAcceptor, messageAcceptor) {
            
            this.callback = callback;
            this.subjectAcceptor = subjectAcceptor;
            this.messageAcceptor = messageAcceptor;
        };
        
        return function() {

            var subscribers = {};
            var subscribersToAnySender = {};

            var sendIfAcceptorMatches = function(subscriber, message, subject) {
                
                var subjectAcceptorMatches = _.isNull(subscriber.subjectAcceptor)
                        || !_.isNull(subscriber.subjectAcceptor) && subscriber.subjectAcceptor(subject);
                var messageAcceptorMatches = _.isNull(subscriber.messageAcceptor)
                || !_.isNull(subscriber.messageAcceptor) && subscriber.messageAcceptor(message);
                
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

            var send = function(senderName, receiverName, subject, message) {

                if (!_.isUndefined(subscribers[receiverName])
                        && !_.isUndefined(subscribers[receiverName][senderName])) {
                    sendToSubscribers(subscribers[receiverName][senderName], message, subject);
                }
                
                if (!_.isUndefined(subscribersToAnySender[receiverName])) {
                    sendToSubscribers(subscribersToAnySender[receiverName], message, subject);
                }
            };
            
            var subscribe = function(senderName, receiverName, callback, subjectAcceptor, messageAcceptor) {

                subjectAcceptor = acceptorFromAcceptorOrSignal(subjectAcceptor);
                messageAcceptor = acceptorFromAcceptorOrSignal(messageAcceptor);

                var subscriber = new Subscriber(callback, subjectAcceptor, messageAcceptor);
                
                if (senderName === Components.ANY) {
                    if (_.isUndefined(subscribersToAnySender[receiverName])){
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
            };

            return {
                send : send,
                subscribe : subscribe
            };
        };
    });
});
