<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Notebook</title>
<script type="text/javascript">
var Properties;
</script>
<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/notebook.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/javascript/lib/require-2.1.11.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/modules/config/requireConfig.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/modules/miniApps/notebook.js" type="text/javascript"></script>
</head>
<body>

    <div id="page-container" class="lingua-component">
        <div id="left-pane">
            <div class="inner">
                <header id="main-header" ng-controller="headerController">
                    <div class="inner">
                        <div id="title-container">
                            <h1>LinguaLearna.com</h1>
                            <button class="lingua-action-button-icon" id="new-notebook-button">New notebook</button>
                        </div>
                        <select id="notebook-selector">
                            <option value="spanish">Spanish & English</option>
                        </select>
                        <ul id="header-tools">
                            <li><a href="#">Resources</a></li>
                            <li id="account-link"><a href="#" class="lingua-icon-link">Oliver Loftus</a></li>
                        </ul>
                    </div>
                </header>
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
