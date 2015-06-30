<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Get bookmarklet</title>
    <link href="${pageContext.request.contextPath}/resources/css/island.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/bookmarklet.css" rel="stylesheet" type="text/css"/>
</head>

<body>

<%@ include file="includes/feedback-button.html" %>

<div id="lingua-page-container">
    <div id="lingua-page-column">
        <%@ include file="includes/page-header.html" %>

        <div id="lingua-page-content">
            <div id="lingua-title-island">
                <h2>Bookmark the LinguaLearna toolbar</h2>
            </div>

            <div id="lingua-page-content-island">
                <p><strong>Please drag the button below to your bookmarks bar.</strong></p>

                <p id="lingua-bookmarklet-wrapper">
                    <a onclick="return false;" id="lingua-bookmarklet" href="javascript:(function(){lingua={};lingua.CsrfSecret='${bookmarklet.csrfSecret}';document.body.appendChild(document.createElement('script')).src='${bookmarklet.bootstrapUrl}';})();">LinguaLearna reader</a>
                </p>

                <p>Now every time you're browsing a website and want to read with LinguaLearna to enable translations, and be able to save notes to your notebooks, just click the bookmarked button!</p>
                <br/>

                <h3>All done?</h3>

                <p>Once you've added the bookmark, click the button below to head over to your notebooks. You'll be prompted to create a new notebook if it's your first one.</p>

                <p>
                    <a id="lingua-generic-to-button" href="${pageContext.request.contextPath}/app/notebooks">To my notebooks</a>
                </p>
                <br/>

                <h3>Wondering where to start?</h3>

                <p>If you've not already had a look and you're a bit lost, see our
                    <strong><a href="http://lingualearna.com/help/getting-started">guide to help you get started</a></strong>.
                </p>
            </div>
        </div>

        <%@ include file="includes/page-footer.html" %>
    </div>
</div>

</body>
</html>
