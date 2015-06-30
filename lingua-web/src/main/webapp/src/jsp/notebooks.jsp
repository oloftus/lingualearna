<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8"/>
    <title>LinguaLearna - Notebook</title>

    <script type="text/javascript">
        var lingua = {};
        lingua.AppRoot = "@app_root@";
        lingua.CsrfToken = "${_csrf.token}";
    </script>

    <link href="${pageContext.request.contextPath}/resources/css/notebook.css" rel="stylesheet" type="text/css">

    <!--(if target dev)>
    <script src="${pageContext.request.contextPath}/javascript/config/require.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/dependencies/requirejs/require.js" data-main="${pageContext.request.contextPath}/javascript/notebook.js" type="text/javascript"></script>
    <!(endif)-->

    <!--(if target prod)>
    <script src="${pageContext.request.contextPath}/js/notebook.min.js" type="text/javascript"></script>
    <!(endif)-->
</head>

<body>

<%@ include file="includes/feedback-button.html" %>

<div ng-controller="notebookController" id="lingua-page-container">
    <div class="lingua-dialog" id="lingua-main-dialog" ui-view="MODAL_DIALOG"></div>
    <div class="lingua-dialog" id="lingua-login-dialog" ui-view="LOGIN_DIALOG"></div>

    <div id="lingua-page-column">
        <div ng-include="global.properties.templatesRoot + '/messages/pageMessages.html'"
             class="lingua-ng-inc"></div>
        <div ng-include="global.properties.templatesRoot + '/panel/notebookHeader.html'"
             class="lingua-ng-inc"></div>

        <div id="lingua-page-content" ui-view></div>
    </div>
</div>

</body>
</html>
