<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Login successful</title>
</head>

<body>

    <script type="text/javascript">
        var loginSuccessSignal = "login-success";
        
        function signalIfLoaded() {
            if (!_.isUndefined(window.parentIFrame)) {
                window.parentIFrame.sendMessage(loginSuccessSignal);
            }
            else {
                setTimeout(function() {
                    signalIfLoaded();
                }, 10);
            }
        }
    </script>
    <script src="${pageContext.request.contextPath}/javascript/lib/underscore-1.6.0.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/javascript/lib/iframeResizer.contentWindow-2.4.3.js"
        type="text/javascript" onload="signalIfLoaded();"></script>
</body>
</html>
