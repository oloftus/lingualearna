<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:ng="http://angularjs.org">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Add/edit note</title>
<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/css/note.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    var initParams = {
        foreignLang : "${param.foreignLang}",
        foreignNote : "${param.foreignNote}",
        localLang : "${param.localLang}",
        localNote : "${param.localNote}",
        additionalNotes : "${param.additionalNotes}",
        sourceUrl : "${param.sourceUrl}",
        noteId : null
    };
</script>
<script data-main="${pageContext.request.contextPath}/javascript/modules/note"
    src="${pageContext.request.contextPath}/javascript/lib/require.js"></script>
<script src="${pageContext.request.contextPath}/javascript/modules/config/requireConfig.js"></script>
</head>

<body ng-controller="noteController">

    <ng-include src="'${pageContext.request.contextPath}/ngViews/noteView.html'" class="lingua-ng-include" />

</body>
</html>