package com.lingualearna.web.settings;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.security.users.UserException;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractController;
import com.lingualearna.web.shared.controller.UserControllerErrorMapper;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;
import com.lingualearna.web.shared.validation.ValidationException;

@Controller
@RequestMapping("/settings")
public class SettingsController extends AbstractController {

    private static final String SETTINGS_MODEL = "settings";
    private static final String SETTINGS_PAGE = "settings";
    private static final String SETTINGS_SAVE_SUCCESS_PAGE = "settingsSaveSuccess";

    @Autowired
    private UserService userService;

    @Autowired
    private UserControllerErrorMapper userErrorMapper;

    @Autowired
    private ObjectMapper<SettingsModel, AppUser> userSettingsMapper;

    @Override
    protected UserService getUserService() {

        return userService;
    }

    @RequestMapping()
    public String settingsPage() throws Exception {

        return SETTINGS_PAGE;
    }

    @ModelAttribute(SETTINGS_MODEL)
    public SettingsModel getSettingsModel(Authentication authentication) {

        SettingsModel settings = new SettingsModel();

        AppUser user = getCurrentUser(authentication);
        userSettingsMapper.copyPropertiesRtl(user, settings, SettingsModel.PASSWORD_FIELD);

        return settings;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processSettingsSubmission(
            Authentication authentication,
            @ModelAttribute(SETTINGS_MODEL) @Valid SettingsModel userSignUp,
            BindingResult bindingResult) throws Exception {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppUser userShell = new AppUser();
        copyPropertiesFromModel(userSignUp, userShell);
        updateUser(bindingResult, userShell, userDetails.getUsername());

        if (bindingResult.hasErrors()) {
            return new ModelAndView(SETTINGS_PAGE);
        }

        return new ModelAndView(SETTINGS_SAVE_SUCCESS_PAGE);
    }

    private void copyPropertiesFromModel(SettingsModel userSignUp, AppUser user) {

        if (userSignUp.getPassword().trim().isEmpty()) {
            userSettingsMapper.copyPropertiesLtr(userSignUp, user, SettingsModel.PASSWORD_FIELD);
        }
        else {
            userSettingsMapper.copyPropertiesLtr(userSignUp, user);
        }
    }

    private void updateUser(BindingResult bindingResult, AppUser userShell, String username) {

        try {
            userService.updateUser(username, userShell);
        }
        catch (UserException | ConstraintViolationException | ValidationException exception) {
            userErrorMapper.populateBindingResultFromException(exception, bindingResult, SETTINGS_MODEL);
        }
    }
}
