<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Notebook</title>

<script type="text/javascript">
    App = {};
    App.Properties = {};

    var csrfToken = "${_csrf.token}";
    var appEntrypoint = "${pageContext.request.contextPath}/javascript/modules/miniApps/notebook.js";

    setTimeout(function() {
        require([ appEntrypoint ]);
    }, 100);
</script>

<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/notebook.css" rel="stylesheet" type="text/css">

<script src="${pageContext.request.contextPath}/javascript/modules/framework/framework.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/lib/require-2.1.11.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/modules/config/requireConfig.js" type="text/javascript"></script>
</head>

<body>

    <div id="page-container" class="lingua-component" ng-controller="notebookRootController">
        <div class="lingua-draggable-dialog" id="lingua-main-dialog">
            <button class="lingua-icon-button lingua-dialog-close" id="lingua-dialog-close-main" ui-sref="NOTEBOOK_MAIN">Close</button>
            <div class="lingua-dialog-view" ui-view="MODAL_DIALOG"></div>
        </div>
        <div id="left-pane">
            <div class="inner">
                <div ng-include="global.properties.ngViewsRoot + '/notebookHeaderView.html'" class="lingua-ng-include"></div>
                <div id="lingua-main-view" ui-view></div>
            </div>
        </div>
        <div id="right-pane">
            <div class="inner">
                <img src="../images/notebook-ad-example.png" alt="Advert" />
            </div>
        </div>
    </div>

</body>
</html>
