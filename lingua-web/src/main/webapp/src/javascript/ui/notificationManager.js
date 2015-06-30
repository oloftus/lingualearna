define(
    [
        "module/ngModule",
        "types/enums",
        "config/properties",
        "localization/strings",
        "underscore",
        "jquery"
    ],
    function (ngModule, enums, properties, strings, _, $) {

        var ERRORS_FIELD = "errors";
        var FIELD_ERRORS_CLASS = "lingua-field-messages";
        var ERROR_CLASS = "lingua-error";

        ngModule.factory("notificationManager",
            [
                "$timeout",

                function ($timeout) {

                    return function($scope) {

                        function getFormMessages() {

                            return $scope.model.formMessages;
                        }

                        function getPageMessages() {

                            return $scope.global.model.pageMessages;
                        }

                        function addMessage(messages, messageText, messageSeverity) {

                            messages.push({
                                type: messageSeverity,
                                text: messageText
                            });
                        }

                        function clearMessages(messages) {

                            messages.length = 0;
                        }

                        function addFormMessage(messageText, messageSeverity) {

                            addMessage(getFormMessages(), messageText, messageSeverity);
                        }

                        function clearFormMessages() {

                            clearMessages(getFormMessages());
                        }

                        function addPageMessage(messageText, messageSeverity) {

                            addMessage(getPageMessages(), messageText, messageSeverity);
                        }

                        function clearPageMessages() {

                            clearMessages(getPageMessages());
                        }

                        function newPageMessage(message, severity) {

                            clearPageMessages();
                            addPageMessage(message, severity);
                        }

                        function newFormMessage(message, severity) {

                            clearFormMessages();
                            addFormMessage(message, severity);
                        }

                        function clearFieldAndFormMessages() {

                            clearFormMessages();
                            $("." + FIELD_ERRORS_CLASS).remove();
                            getElementsWithDataErrors().removeData(ERRORS_FIELD);
                        }

                        function getField(fieldName) {

                            var fieldSelector = "[data-fieldName='" + fieldName + "']";
                            return $(fieldSelector);
                        }

                        function addFieldError(fieldName, errorText) {

                            var $field = getField(fieldName);
                            if (_.isUndefined($field.data(ERRORS_FIELD))) {
                                $field.data(ERRORS_FIELD, []);
                            }

                            $field.data(ERRORS_FIELD).push(errorText);
                        }

                        function getElementsWithDataErrors() {

                            return $("*").filter(function () {
                                return !_.isUndefined($(this).data(ERRORS_FIELD));
                            });
                        }

                        function displayFieldMessages(fieldNames) {

                            _.each(fieldNames, function (fieldName) {
                                var $field = getField(fieldName);
                                var errors = $field.data(ERRORS_FIELD);

                                var $errorsRoot = $("<div class='" + FIELD_ERRORS_CLASS + "'/>");
                                var $errorsUl = $("<ul/>").appendTo($errorsRoot);

                                _.each(errors, function (errorText) {
                                    $errorsUl.append("<li class='" + ERROR_CLASS + "'>" + errorText + "</li>");
                                });

                                $field.after($errorsRoot);
                            });
                        }

                        function handleValidationErrors(errors) {

                            clearFieldAndFormMessages();

                            _.each(errors.fieldErrors, function (errors, fieldName) {
                                _.each(errors, function (errorText) {
                                    addFieldError(fieldName, errorText);
                                });
                            });
                            _.each(errors.globalErrors, function (errorText) {
                                addFormMessage(errorText, enums.MessageSeverity.ERROR);
                            });

                            displayFieldMessages(_.keys(errors.fieldErrors));
                        }

                        function handleErrors(data, status) {

                            if (status === enums.HttpHeaders.BAD_REQUEST && !_.isUndefined(data.fieldErrors) && !_.isUndefined(data.globalErrors)) {
                                handleValidationErrors(data);
                            }
                            else {
                                newFormMessage(strings.genericServerErrorMessage,
                                    enums.MessageSeverity.ERROR);
                            }
                        }

                        function setup() {

                            var pageMessageTimeout = null;
                            $scope.$watchCollection("global.model.pageMessages", function (newMessages, oldMessages) {

                                if (newMessages.length > oldMessages.length) {
                                    if (!_.isNull(pageMessageTimeout)) {
                                        $timeout.cancel(pageMessageTimeout);
                                    }
                                    pageMessageTimeout = $timeout(function () {
                                        clearPageMessages();
                                    }, properties.pageMessagesTimeout);
                                }
                            });
                        }

                        function doExport() {

                            this.newFormMessage = newFormMessage;
                            this.newPageMessage = newPageMessage;
                            this.handleErrors = handleErrors;
                            this.setup = setup;
                        }
                        doExport.call(this);
                    }
                }
            ]);
    });
