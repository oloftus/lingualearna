<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:form="http://www.springframework.org/tags/form"
      xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Sign up</title>
    <link href="${pageContext.request.contextPath}/resources/css/island.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        #lingua-page-content-island {
            margin-top: 20px;
        }
    </style>
</head>

<body>

<%@ include file="includes/feedback-button.html" %>

<div id="lingua-page-container">
    <div id="lingua-page-column">
        <%@ include file="includes/page-header.html" %>

        <div id="lingua-page-content">
            <div id="lingua-title-island">
                <h2>Sign up to LinguaLearna</h2>
            </div>

            <div id="lingua-page-content-island">
                <form:form commandName="userSignup" cssClass="lingua-form">
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
                            <form:errors path="firstName" cssClass="lingua-error"/>
                        </div>
                        <div class="lingua-column">
                            <h3>
                                <label for="lastName">Last name</label>
                            </h3>
                            <form:input path="lastName" id="lastName"/>
                            <form:errors path="lastName" cssClass="lingua-error"/>
                        </div>
                    </div>
                    <div class="lingua-row">
                        <div class="lingua-column">
                            <h3>
                                <label for="emailAddress">Email address</label>
                            </h3>
                            <form:input path="emailAddress" type="email" id="emailAddress"/>
                            <form:errors path="emailAddress" cssClass="lingua-error"/>
                        </div>
                        <div class="lingua-column">
                            <h3>
                                <label for="password">Password</label>
                            </h3>
                            <form:input path="password" type="password" id="password"/>
                            <form:errors path="password" cssClass="lingua-error"/>
                        </div>
                    </div>
                    <div class="lingua-row">
                        <div class="lingua-column">
                            <h3>
                                <label for="confirmEmailAddress">Confirm email address</label>
                            </h3>
                            <form:input path="confirmEmailAddress" type="email" id="confirmEmailAddress"/>
                            <form:errors path="confirmEmailAddress" cssClass="lingua-error"/>
                        </div>
                        <div class="lingua-column">
                            <h3>
                                <label for="confirmPassword">Confirm password</label>
                            </h3>
                            <form:input path="confirmPassword" type="password" id="confirmPassword"/>
                            <form:errors path="confirmPassword" cssClass="lingua-error"/>
                        </div>
                    </div>
                    <div class="lingua-row">
                        <h3 id="recaptcha-header">
                            <label for="recaptcha_response_field">Please enter the text below</label>
                        </h3>

                        <div style="width:450px;">
                            <c:set var="recaptchaPublicKey" value="${recaptcha.publicKey}"/>
                            <c:set var="recaptchaPrivateKey" value="${recaptcha.privateKey}"/>
                            <%
                            String recaptchaPublicKey = (String) pageContext.getAttribute("recaptchaPublicKey");
                            String recaptchaPrivateKey = (String) pageContext.getAttribute("recaptchaPrivateKey");
                            String recpatchaTheme = "clean";
                            ReCaptcha c = ReCaptchaFactory.newSecureReCaptcha(recaptchaPublicKey, recaptchaPrivateKey, false);
                            out.print(c.createRecaptchaHtml(null, recpatchaTheme, null));
                            %>
                            <form:errors path="captchaPlaceholder" cssClass="lingua-error"/>
                        </div>
                    </div>
                    <div class="lingua-controls">
                        <input type="submit" class="lingua-primary-button" value="Sign up"/>
                    </div>
                </form:form>
            </div>
        </div>

        <%@ include file="includes/page-footer.html" %>
    </div>
</div>

</body>
</html>
