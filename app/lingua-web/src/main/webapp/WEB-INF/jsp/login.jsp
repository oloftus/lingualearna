<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF8" />
<title>LinguaLearna - Login</title>
<link href="${pageContext.request.contextPath}/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/javascript/lib/jquery-2.1.0.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function() {
        
        var usernameText = "Username";
        var passwordText = "Password";
        var blankValue = "";
        var usernameField = $("#lingua-username");
        var passwordField = $("#lingua-password");
        
        usernameField.val(usernameText);
        passwordField.val(passwordText);
        
        usernameField.click(function() {
            var $this = $(this);
            if ($this.val() === usernameText) {
                $this.val(blankValue);
            }
        });
        usernameField.blur(function() {
            var $this = $(this);
            if ($this.val() === blankValue) {
                $this.val(usernameText);
            }
        });
        passwordField.click(function() {
            var $this = $(this);
            if ($this.val() === passwordText) {
                $this.val(blankValue);
            }
        });
        passwordField.blur(function() {
            var $this = $(this);
            if ($this.val() === blankValue) {
                $this.val(passwordText);
            }
        });
    });
</script>
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
                <input type="text" name="j_username" value="" id="lingua-username" />
                <input type="text" name="j_password" value="" id="lingua-password" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </div>
            <div class="lingua-dialog-footer">
                <input name="submit" type="submit" value="Login" class="lingua-action-button-icon"
                    id="lingua-login-button" />
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/javascript/lib/iframeResizer.contentWindow-2.4.3.js"
        type="text/javascript"></script>
</body>
</html>
