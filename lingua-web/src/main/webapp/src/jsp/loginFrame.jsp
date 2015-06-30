<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Login</title>
    <link href="${pageContext.request.contextPath}/resources/css/loginFrame.css" rel="stylesheet" type="text/css">
</head>

<body>

<form action="${pageContext.request.contextPath}/app/loginFrame/process" method="POST">
    <%@ include file="includes/login.html" %>
</form>

<script src="${pageContext.request.contextPath}/dependencies/iframe-resizer/js/iframeResizer.contentWindow.min.js"
        type="text/javascript"></script>
</body>
</html>
