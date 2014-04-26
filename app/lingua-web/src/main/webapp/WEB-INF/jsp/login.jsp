<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Login</title>
<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet" type="text/css" />
</head>

<body>

    <div class="lingua-component lingua-dialog-inner" id="lingua-login-dialog">
        <form action="${pageContext.request.contextPath}/j_spring_security_check" method="POST">
            <div class="lingua-dialog-header">
                <h2>Login</h2>
            </div>
            <div class="lingua-dialog-content">
                <c:if test="${!empty param['loginFailed']}">
                    <div id="lingua-global-messages">
                        <ul>
                            <li class="lingua-error">Your username/password was incorrect. Please try again.</li>
                        </ul>
                    </div>
                </c:if>
                <input type="text" name="j_username" value="Username" id="lingua-username" />
                <input type="text" name="j_password" value="Password" id="lingua-password" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </div>
            <div class="lingua-dialog-footer">
                <input name="submit" type="submit" value="Login" class="lingua-action-button-icon"
                    id="lingua-login-button" />
            </div>
        </form>
    </div>

</body>
</html>
