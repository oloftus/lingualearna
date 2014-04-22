(function(){
    
    var dependencies = [ "linguaApp", "underscore", "jquery" ];
    
    define(dependencies, function(linguaApp, _, $) {
        
        var messageHandler = function() {
            
            var ERRORS_FIELD = "errors";
            var FIELD_ERRORS_CLASS = "lingua-field-messages";
            var ERROR_CLASS = "lingua-error";
            
            var addGlobalMessage = function(scope, messageText, messageSeverity) {
                
                scope.model.globalMessages.push({
                    type : messageSeverity,
                    text : messageText
                });
            };
            
            var getField = function(fieldName) {
                
                var fieldSelector = "[data-fieldName='" + fieldName + "']";
                return $(fieldSelector);
            };
            
            var addFieldError = function(fieldName, errorText) {
                
                $field = getField(fieldName);
                if (_.isUndefined($field.data(ERRORS_FIELD))) {
                    $field.data(ERRORS_FIELD, []);
                }
                
                $field.data(ERRORS_FIELD).push(errorText);
            };
            
            var getElementsWithDataErrors = function() {
                
                return $("*").filter(function(index) {
                    return !_.isUndefined($(this).data(ERRORS_FIELD));
                });
            };
            
            var clearAllMessages = function(scope) {
                
                scope.model.globalMessages.length = 0;
                $("." + FIELD_ERRORS_CLASS).remove();
                getElementsWithDataErrors().removeData(ERRORS_FIELD);
            };
            
            var displayFieldMessage = function(fieldNames) {
                
                _.each(fieldNames, function(fieldName) {
                    var $field = getField(fieldName);
                    var errors = $field.data(ERRORS_FIELD);
                    
                    var $errorsRoot = $("<div class='" + FIELD_ERRORS_CLASS + "'/>");
                    var $errorsUl = $("<ul/>").appendTo($errorsRoot);
                    
                    _.each(errors, function(errorText) {
                        $errorsUl.append("<li class='" + ERROR_CLASS + "'>" + errorText + "</li>")
                    });
                    
                    $field.after($errorsRoot);
                });
            };
            
            var handleValidationErrors = function(scope, errors) {
                
                clearAllMessages(scope);
                
                _.each(errors.fieldErrors, function(errors, fieldName) {
                    _.each(errors, function(errorText) {
                        addFieldError(fieldName, errorText);
                    });
                });
                _.each(errors.globalErrors, function(errorText) {
                    addGlobalMessage(scope, errorText, MessageSeverity.ERROR);
                });
                
                displayFieldMessage(_.keys(errors.fieldErrors));
            };
            
            var handleErrors = function(scope, data, status, headers) {
                
                if (status == HttpHeaders.BAD_REQUEST && !_.isUndefined(data.fieldErrors)
                        && !_.isUndefined(data.globalErrors)) {
                    handleValidationErrors(scope, data);
                }
            };
            
            addFreshGlobalMessage = function(scope, message, severity) {

                clearAllMessages(scope);
                addGlobalMessage(scope, message, severity);
            };
            
            return {
                addFreshGlobalMessage : addFreshGlobalMessage,
                addGlobalMessage : addGlobalMessage,
                handleErrors : handleErrors,
                clearAllMessages : clearAllMessages
            };
        };
        
        linguaApp.service("messageHandler", [ messageHandler ]);
    });
})();
