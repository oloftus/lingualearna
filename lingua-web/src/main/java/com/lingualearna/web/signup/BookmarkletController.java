package com.lingualearna.web.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lingualearna.web.security.CsrfService;
import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractController;

@Controller
@RequestMapping("/bookmarklet")
public class BookmarkletController extends AbstractController {

    private static final String BOOKMARKLET_MODEL = "bookmarklet";
    private static final String BOOTSTRAP_URL = "bootstrapUrl";
    private static final String CSRF_SECRET = "csrfSecret";

    @Value("${environment.reader.bootstrapUrl}")
    private String bootstrapUrl;

    @Autowired
    private CsrfService csrfService;

    @Autowired
    private UserService userService;

    @RequestMapping()
    public String bookmarkletPage() {

        return BOOKMARKLET_MODEL;
    }

    @ModelAttribute(BOOKMARKLET_MODEL)
    public Model getBookmarkletModel(Authentication authentication) {

        AppUser currentUser = getCurrentUser(authentication);

        Model model = new ExtendedModelMap();
        model.addAttribute(CSRF_SECRET, csrfService.getCsrfSecret(currentUser));
        model.addAttribute(BOOTSTRAP_URL, bootstrapUrl);

        return model;
    }

    @Override
    protected UserService getUserService() {

        return userService;
    }
}
