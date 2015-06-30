<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:form="http://www.springframework.org/tags/form"
      xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Settings</title>
    <link href="${pageContext.request.contextPath}/resources/css/settings.css" rel="stylesheet" type="text/css"/>
</head>

<body>

<div>
    <form:form commandName="settings" cssClass="lingua-form">
        <c:set var="globalErrors">
            <form:errors path=""/>
        </c:set>
        <c:if test="${not empty globalErrors}">
            <div class="lingua-form-messages">
                <form:errors path="" cssClass="lingua-error"/>
            </div>
        </c:if>
        <div class="lingua-row">
            <div class="lingua-column">
                <h3>
                    <label for="firstName">First name</label>
                </h3>
                <form:input path="firstName" id="firstName"/>
                <c:set var="firstNameErrors">
                    <form:errors path="firstName"/>
                </c:set>
                <c:if test="${not empty firstNameErrors}">
                    <div class="lingua-field-messages">
                        <ul>
                            <li class="lingua-error">
                                <form:errors path="firstName" cssClass="lingua-error"/>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
            <div class="lingua-column">
                <h3>
                    <label for="lastName">Last name</label>
                </h3>
                <form:input path="lastName" id="lastName"/>
                <c:set var="lastNameErrors">
                    <form:errors path="lastName"/>
                </c:set>
                <c:if test="${not empty lastNameErrors}">
                    <div class="lingua-field-messages">
                        <ul>
                            <li class="lingua-error">
                                <form:errors path="lastName" cssClass="lingua-error"/>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="lingua-row">
            <h3>
                <label for="emailAddress">Email address</label>
            </h3>
            <form:input path="emailAddress" type="email" id="emailAddress"/>
            <c:set var="emailAddressErrors">
                <form:errors path="emailAddress"/>
            </c:set>
            <c:if test="${not empty emailAddressErrors}">
                <div class="lingua-field-messages">
                    <ul>
                        <li class="lingua-error">
                            <form:errors path="emailAddress" cssClass="lingua-error"/>
                        </li>
                    </ul>
                </div>
            </c:if>
        </div>
        <div class="lingua-row">
            <h3>
                <label for="password">Password</label>
            </h3>
            <form:input path="password" type="password" id="password"/>
            <c:set var="passwordErrors">
                <form:errors path="password"/>
            </c:set>
            <c:if test="${not empty passwordErrors}">
                <div class="lingua-field-messages">
                    <ul>
                        <li class="lingua-error">
                            <form:errors path="password" cssClass="lingua-error"/>
                        </li>
                    </ul>
                </div>
            </c:if>
        </div>
        <div class="lingua-row">
            <h3>
                <label for="confirmPassword">Confirm password</label>
            </h3>
            <form:input path="confirmPassword" type="password" id="confirmPassword"/>
            <c:set var="confirmPasswordErrors">
                <form:errors path="confirmPassword"/>
            </c:set>
            <c:if test="${not empty confirmPasswordErrors}">
                <div class="lingua-field-messages">
                    <ul>
                        <li class="lingua-error">
                            <form:errors path="confirmPassword" cssClass="lingua-error"/>
                        </li>
                    </ul>
                </div>
            </c:if>
        </div>
        <div class="lingua-controls">
            <input type="submit" class="lingua-primary-button" value="Save settings"/>
        </div>
    </form:form>
</div>

<script src="${pageContext.request.contextPath}/dependencies/iframe-resizer/js/iframeResizer.contentWindow.min.js"
        type="text/javascript"></script>
</body>
</html>
