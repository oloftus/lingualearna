package com.lingualearna.web.signup;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.security.users.UserException;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.controller.UserControllerErrorMapper;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;
import com.lingualearna.web.shared.validation.ValidationException;

@Controller
@RequestMapping(SignupController.SIGNUP_CONTROLLER_URL)
public class SignupController {

    public static final String SIGNUP_CONTROLLER_URL = "/signup";
    public static final String EMAIL_VALIDATION_KEY_PARAM = "{emailValidationKey}";
    private static final String VERIFY_URL_FRAGMENT = "/verify/" + EMAIL_VALIDATION_KEY_PARAM;
    public static final String VERIFY_URL = SIGNUP_CONTROLLER_URL + VERIFY_URL_FRAGMENT;

    private static final String RECAPTCHA_CHALLENGE_FIELD = "recaptcha_challenge_field";
    private static final String RECAPTCHA_RESPONSE_FIELD = "recaptcha_response_field";
    private static final String RECAPTCHA_KEYS = "recaptcha";
    private static final String RECAPTCHA_PUBLIC_KEY = "publicKey";
    private static final String RECAPTCHA_PRIVATE_KEY = "privateKey";

    private static final String USER_SIGNUP_MODEL = "userSignup";
    private static final String CAPTCHA_PLACEHOLDER_FIELD = "captchaPlaceholder";
    private static final String VALIDATION_SUCCESS_FIELD = "validationSuccess";

    private static final String SIGNUP_PAGE = "signup";
    private static final String SIGNUP_SUCCESS_PAGE = "signupSuccess";
    private static final String VALIDATE_EMAIL_PAGE = "verifyEmail";

    private static final String INVALID_CAPTCHA_MSG = "Please enter the text correctly";

    @Value("${security.recaptcha.privateKey}")
    private String recaptchaPrivateKey;

    @Value("${security.recaptcha.publicKey}")
    private String recaptchaPublicKey;

    @Autowired
    private UserService userService;

    @Autowired
    private UserControllerErrorMapper userErrorMapper;

    @Autowired
    private ObjectMapper<SignupModel, AppUser> signupModelMapper;

    @RequestMapping()
    public ModelAndView signupPage() throws Exception {

        Map<String, Object> model = new HashMap<>();

        return new ModelAndView(SIGNUP_PAGE, model);
    }

    @ModelAttribute(USER_SIGNUP_MODEL)
    public SignupModel getSignupModel() {

        return new SignupModel();
    }

    @ModelAttribute(RECAPTCHA_KEYS)
    public Model getRecaptchaModel() {

        Model model = new ExtendedModelMap();
        model.addAttribute(RECAPTCHA_PRIVATE_KEY, recaptchaPrivateKey);
        model.addAttribute(RECAPTCHA_PUBLIC_KEY, recaptchaPublicKey);

        return model;
    }

    @RequestMapping(value = VERIFY_URL_FRAGMENT)
    public ModelAndView processEmailValidation(@PathVariable String emailValidationKey) throws Exception {

        boolean validationSuccess = userService.enableUserByValidationKey(emailValidationKey);

        Map<String, Object> model = new HashMap<>();
        model.put(VALIDATION_SUCCESS_FIELD, validationSuccess);

        return new ModelAndView(VALIDATE_EMAIL_PAGE, model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processUserSignup(
            ServletRequest request,
            @RequestParam(RECAPTCHA_CHALLENGE_FIELD) String captchaChallange,
            @RequestParam(RECAPTCHA_RESPONSE_FIELD) String captchaResponse,
            @ModelAttribute(USER_SIGNUP_MODEL) @Valid SignupModel userSignUp,
            BindingResult bindingResult) throws Exception {

        boolean captchaValid = processCaptchaResponse(bindingResult, request, captchaChallange, captchaResponse);

        if (captchaValid) {
            createUser(userSignUp, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return signupPage();
        }

        return new ModelAndView(SIGNUP_SUCCESS_PAGE);
    }

    private void createUser(SignupModel userSignUp, BindingResult bindingResult) {

        AppUser user = createUserObjFromModel(userSignUp);

        try {
            userService.createUser(user);
        }
        catch (UserException | ConstraintViolationException | ValidationException exception) {
            userErrorMapper.populateBindingResultFromException(exception, bindingResult, USER_SIGNUP_MODEL);
        }
    }

    private AppUser createUserObjFromModel(SignupModel userSignUp) {

        AppUser user = new AppUser();
        signupModelMapper.copyPropertiesLtr(userSignUp, user);

        return user;
    }

    private boolean isCaptchaValid(ServletRequest request, String challenge, String response) {

        String remoteAddr = request.getRemoteAddr();

        ReCaptchaImpl recaptcha = new ReCaptchaImpl();
        recaptcha.setPrivateKey(recaptchaPrivateKey);

        ReCaptchaResponse result = recaptcha.checkAnswer(remoteAddr, challenge, response);

        return result.isValid();
    }

    private boolean processCaptchaResponse(BindingResult bindingResult, ServletRequest request,
            String captchaChallange, String captchaResponse) {

        boolean captchaValid = isCaptchaValid(request, captchaChallange, captchaResponse);

        if (!captchaValid) {
            FieldError captchaError = new FieldError(USER_SIGNUP_MODEL, CAPTCHA_PLACEHOLDER_FIELD,
                    INVALID_CAPTCHA_MSG);
            bindingResult.addError(captchaError);
        }

        return captchaValid;
    }
}
