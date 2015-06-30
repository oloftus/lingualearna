<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Verify email address</title>
    <link href="${pageContext.request.contextPath}/resources/css/island.css" rel="stylesheet" type="text/css"/>
</head>

<body xmlns:c="http://java.sun.com/jsp/jstl/core">

<%@ include file="includes/feedback-button.html" %>

<div id="lingua-page-container">
    <div id="lingua-page-column">
        <%@ include file="includes/page-header.html" %>

        <div id="lingua-page-content">
            <div id="lingua-page-content-island">
                <c:choose>
                    <c:when test="${validationSuccess}">
                        <h2>You've successfully verified your email address</h2>

                        <p><strong>Now click the button below to get the LinguaLearna toolbar button.</strong></p>

                        <p>This lets you translate text and add notes to your notebook when reading
                            <em>any webpage</em>, all at the click of a button!</p>

                        <p>
                            <a id="lingua-generic-to-button" href="${pageContext.request.contextPath}/app/bookmarklet">Get the button</a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <h2>Your email verification key was incorrect</h2>

                        <p>Sorry, it looks like something went wrong along the way.</p>

                        <p>Please send us an email at
                            <a href="mailto:support@lingualearna.com">support@lingualearna.com</a> and we'll sort it out for you.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%@ include file="includes/page-footer.html" %>
    </div>
</div>

</body>
</html>
