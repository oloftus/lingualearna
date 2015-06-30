package com.lingualearna.web.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractController;

@Controller
@RequestMapping
public class SecurityController extends AbstractController {

    private static final String CSRF_TOKEN = "_csrf";

    @Autowired
    private UserService userService;

    @Autowired
    private CsrfService csrfService;

    @Override
    protected UserService getUserService() {

        return userService;
    }

    @RequestMapping("/login")
    public String loginPage() {

        return "login";
    }

    @RequestMapping("/loginFrame")
    public String loginFramePage() {

        return "loginFrame";
    }

    @RequestMapping("/loginFrame/success")
    public String loginFrameSuccessPage() {

        return "loginFrameSuccess";
    }

    @RequestMapping("/logout/success")
    public String logoutSuccessPage() {

        return "logoutSuccess";
    }

    @RequestMapping(value = "/api/ping", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String ping(HttpServletRequest request, Principal principal) {

        return new DateTime().toString();
    }

    @RequestMapping(value = "/api/security/csrfToken/{secret}", produces = "application/text", method = RequestMethod.GET)
    @ResponseBody
    public String getCsrfToken(Authentication authentication, HttpServletRequest request, @PathVariable String secret) {

        csrfService.assertSecretCorrect(getCurrentUser(authentication), secret);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN);
        return csrfToken.getToken();
    }
}
