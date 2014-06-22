App.Factory.createNew(function() {

    this.isCalled("messageHandler");
    
    this.imports("underscore");
    this.imports("jquery");

    this.hasDefinition(function(_, $) {

        var ERRORS_FIELD = "errors";
        var FIELD_ERRORS_CLASS = "lingua-field-messages";
        var ERROR_CLASS = "lingua-error";
        
        return function() {

            var getGlobalMessages = function($scope) {

                return $scope.model.globalMessages;
            };

            var getPageMessages = function($scope) {

                return $scope.global.model.pageMessages;
            };

            var addGlobalOrPageMessage = function(messages, messageText, messageSeverity) {

                messages.push({
                    type : messageSeverity,
                    text : messageText
                });
            };

            var clearGlobalOrPageMessages = function(messages) {

                messages.length = 0;
            };

            var addGlobalMessage = function($scope, messageText, messageSeverity) {

                addGlobalOrPageMessage(getGlobalMessages($scope), messageText, messageSeverity);
            };

            var clearGlobalMessages = function($scope) {

                clearGlobalOrPageMessages(getGlobalMessages($scope));
            };

            var addPageMessage = function($scope, messageText, messageSeverity) {

                addGlobalOrPageMessage(getPageMessages($scope), messageText, messageSeverity);
            };

            var clearPageMessages = function($scope) {

                clearGlobalOrPageMessages(getPageMessages($scope));
            };

            var addFreshPageMessage = function($scope, message, severity) {

                clearGlobalOrPageMessages($scope);
                addPageMessage($scope, message, severity);
            };

            var addFreshGlobalMessage = function($scope, message, severity) {

                clearLocalAndGlobalMessages($scope);
                addGlobalMessage($scope, message, severity);
            };

            var clearLocalAndGlobalMessages = function($scope) {

                clearGlobalMessages($scope);
                $("." + FIELD_ERRORS_CLASS).remove();
                getElementsWithDataErrors().removeData(ERRORS_FIELD);
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

            var handleValidationErrors = function($scope, errors) {

                clearLocalAndGlobalMessages($scope);

                _.each(errors.fieldErrors, function(errors, fieldName) {
                    _.each(errors, function(errorText) {
                        addFieldError(fieldName, errorText);
                    });
                });
                _.each(errors.globalErrors, function(errorText) {
                    addGlobalMessage($scope, errorText, MessageSeverity.ERROR);
                });

                displayFieldMessage(_.keys(errors.fieldErrors));
            };

            var handleErrors = function($scope, data, status, headers) {

                if (status === HttpHeaders.BAD_REQUEST && !_.isUndefined(data.fieldErrors)
                        && !_.isUndefined(data.globalErrors)) {
                    handleValidationErrors($scope, data);
                }
                else {
                    addFreshGlobalMessage($scope, LocalStrings.genericServerErrorMessage, MessageSeverity.ERROR);
                }
            };

            return {
                addFreshGlobalMessage : addFreshGlobalMessage,
                clearGlobalMessages : clearGlobalMessages,
                addFreshPageMessage : addFreshPageMessage,
                clearPageMessages : clearPageMessages,
                handleErrors : handleErrors,
                clearLocalAndGlobalMessages : clearLocalAndGlobalMessages
            };
        };
    });
});
