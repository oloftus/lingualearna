<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Error</title>
    <link href="${pageContext.request.contextPath}/resources/css/island.css" rel="stylesheet" type="text/css"/>
</head>

<body>

<div id="lingua-page-container">
    <div id="lingua-page-column">
        <%@ include file="includes/page-header.html" %>

        <div id="lingua-page-content">
            <div id="lingua-page-content-island">
                <h2>Something went wrong</h2>

                <p>Sorry, it looks like we're having some problems at the moment. Please try again later.</p>

                <p>If you keep getting the same problem please email us at
                    <a href="mailto:support@lingualearna.com">support@lingualearna.com</a> mentioning the date and time
                    you saw this page and we'll investigate it. Thanks.</p>
            </div>

            <!-- ${stacktrace} -->
        </div>

        <%@ include file="includes/page-footer.html" %>
    </div>
</div>

</body>
</html>
