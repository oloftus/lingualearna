<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:c="http://java.sun.com/jsp/jstl/core"">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Login</title>
    <link href="${pageContext.request.contextPath}/resources/css/notebook.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/island.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet" type="text/css"/>
</head>

<body>

<%@ include file="includes/feedback-button.html" %>

<div id="lingua-page-container">
    <div id="lingua-page-column">
        <%@ include file="includes/page-header.html" %>

        <div id="lingua-page-content">
            <div class="lingua-dialog" id="lingua-login-dialog">
                <div class="lingua-inner">
                    <div class="lingua-dialog-header">
                        <h2>Login</h2>
                    </div>
                    <form action="${pageContext.request.contextPath}/app/login/process" method="POST">
                        <%@ include file="includes/login.html" %>
                    </form>
                </div>
            </div>

            <div id="lingua-login-help">
                <h3>Not got an account?</h3>

                <p>You can <a href="${pageContext.request.contextPath}/app/signup">signup here</a></p>

                <h3>Forgotten your password?</h3>

                <p>Just send us a quick email at
                    <a href="mailto:support@lingualearna.com">support@lingualearna.com</a> and we'll be happy to reset it for you
                </p></div>
            </ul>
        </div>

        <%@ include file="includes/page-footer.html" %>
    </div>
</div>

</body>
</html>
