define([ "angular", "appRoot", "underscore.string" ], function() {

    var MessageHandlers = function() {

        var _s = require("underscore.string"); // TODO: Make better solution
        
        var addGlobalMessage = function(scope, messageText, messageSeverity) {

            scope.model.globalMessages.push({
                type : messageSeverity,
                text : messageText
            });
        };

        var getField = function(fieldName) {
            var fieldSelector = _s.sprintf("textarea[data-fieldName='%s']", fieldName);
            return $(fieldSelector);
        };
        
        var addFieldError = function(fieldName, errorText) {
            
            $field = getField(fieldName);
            if (_.isUndefined($field.data("errors"))) {
                $field.data("errors", []);
            }
            
            $field.data("errors").push(errorText);
        };
        
        var getElementsWithDataErrors = function() {
            
            return $("*").filter(function(index) {
                return $(this).data("errors") != undefined;
            });
        };
        
        var clearAllMessages = function(scope) {
            
            scope.model.globalMessages.length = 0;
            $(".lingua-field-messages").remove();
            getElementsWithDataErrors().removeData("errors")
        };
        
        var displayFieldMessage = function(fieldNames) {
            
            _.each(fieldNames, function(fieldName) {
                var $field = getField(fieldName);
                var errors = $field.data("errors");
                
                var $errorsRoot = $("<div class='lingua-field-messages'/>");
                var $errorsUl = $("<ul/>").appendTo($errorsRoot);
                
                _.each(errors, function(errorText) {
                    $errorsUl.append("<li class='lingua-error'>" + errorText + "</li>")
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

        return {
            addGlobalMessage : addGlobalMessage,
            handleErrors : handleErrors,
            clearAllMessages : clearAllMessages
        };
    };

    linguaApp.service("messageHandlers", [ MessageHandlers ]);
});
