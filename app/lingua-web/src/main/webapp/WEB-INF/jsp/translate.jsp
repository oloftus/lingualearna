<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Translate selection</title>
<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/selection-translation-dialog.css" rel="stylesheet"
    type="text/css" />
<script type="text/javascript">
    var initParams = {
        sourceLang : "${param.sourceLang}",
        targetLang : "${param.targetLang}",
        query : "${param.query}"
    };
</script>
<script src="${pageContext.request.contextPath}/javascript/modules/config/requireConfig.js"></script>
<script data-main="${pageContext.request.contextPath}/javascript/modules/miniApps/translate"
    src="${pageContext.request.contextPath}/javascript/lib/require-2.1.11.js"></script>
</head>

<body ng-controller="translateController">

    <ng-include src="'${pageContext.request.contextPath}/ngViews/translateView.html'" class="lingua-ng-include" />

</body>
</html>