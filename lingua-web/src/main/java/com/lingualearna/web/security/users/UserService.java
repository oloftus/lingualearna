package com.lingualearna.web.security.users;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lingualearna.web.security.emailvalidation.ValidationEmailSender;
import com.lingualearna.web.shared.components.AbstractService;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;
import com.lingualearna.web.shared.utility.Clock;
import com.lingualearna.web.shared.validation.validators.PasswordValidator;

@Service
@Transactional
public class UserService extends AbstractService {

    private static final String INVALID_WDYHAU_ERROR_MSG = "Invalid 'where did you hear about us' value";
    private static final String DUPLICATE_USER_ERROR_MSG = "A user with this username/email address already exists";
    private static final int EMAIL_VALIDATION_KEY_LENGTH = 30;

    @Autowired
    private ValidationEmailSender validationEmailSender;

    @Autowired
    private Validator validator;

    @Autowired
    private Clock clock;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private ObjectMapper<AppUser, AppUser> userMapper;

    private PasswordEncoder passwordEncoder;

    public PasswordEncoder getPasswordEncoder() {

        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Validator getValidator() {

        return validator;
    }

    public void updateUser(String username, AppUser userShell) {

        AppUser userEntity = getUserByUsername(username);

        String oldEmailAddress = userEntity.getEmailAddress();
        String oldPassword = userEntity.getPassword();

        copyProperties(userShell, userEntity);

        String newEmailAddress = userEntity.getEmailAddress();
        String newPassword = userEntity.getPassword();

        if (changed(oldPassword, newPassword)) {
            validatePassword(newPassword);
            hashPassword(userEntity);
        }

        validateEntity(userEntity);

        if (changed(oldEmailAddress, newEmailAddress)) {
            checkEmailAddressIsNotRegistered(newEmailAddress);
            updateCurrentPrincipal(userEntity, newEmailAddress);
        }

        userDao.merge(userEntity);
    }

    public AppUser getUserByUsername(String username) {

        return userDao.findByUsername(username);
    }

    public boolean enableUserByValidationKey(String emailValidationKey) {

        AppUser user = userDao.findByEmailValidationKey(emailValidationKey);
        boolean emailKeyExists = user != null;

        if (emailKeyExists) {
            user.setEnabled(true);
            user.setEmailValidationKey(null);
            userDao.merge(user);
        }

        return emailKeyExists;
    }

    public void createUser(AppUser user) {

        checkEmailAddressIsNotRegistered(user.getEmailAddress());
        validatePassword(user.getPassword());
        hashPassword(user);
        setUserProperties(user);
        validateEntity(user);
        userDao.persist(user);
        validationEmailSender.send(user);
    }

    private void copyProperties(AppUser userShell, AppUser userEntity) {

        userEntity.setFirstName(userShell.getFirstName());
        userEntity.setLastName(userShell.getLastName());
        userEntity.setEmailAddress(userShell.getEmailAddress());

        if (passwordChanged(userShell)) {
            userEntity.setPassword(userShell.getPassword());
        }
    }

    private boolean passwordChanged(AppUser userShell) {

        return userShell.getPassword() != null && !userShell.getPassword().trim().isEmpty();
    }

    private boolean changed(String oldPassword, String newPassword) {

        return !newPassword.equals(oldPassword);
    }

    private void checkEmailAddressIsNotRegistered(String emailAddress) {

        if (emailAddress.trim().isEmpty()) {
            return;
        }

        boolean userExists = userDao.userExists(emailAddress);

        if (userExists) {
            throw new UserException(DUPLICATE_USER_ERROR_MSG);
        }
    }

    private void validatePassword(String password) {

        passwordValidator.validate(password);
    }

    private void hashPassword(AppUser user) {

        String encryptedPassword = getPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    public List<AppUser> getAllUsers() {

        return userDao.getAllUsers();
    }

    private String getEmailValidationKey(AppUser user) {

        String randomString = RandomStringUtils.randomAlphanumeric(EMAIL_VALIDATION_KEY_LENGTH);
        String hashedUsername = DigestUtils.md2Hex(user.getEmailAddress());

        return randomString + hashedUsername;
    }

    private void setUserProperties(AppUser user) {

        user.setEnabled(false);
        user.setRole(Role.ROLE_USER);
        user.setDateTimeCreated(clock.now());
        user.setEmailValidationKey(getEmailValidationKey(user));
    }

    private void updateCurrentPrincipal(AppUser user, String newEmailAddress) {

        Authentication originalAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails originalUserDetails = (UserDetails) originalAuth.getPrincipal();

        UserDetails newUserDetails = new User(newEmailAddress, user.getPassword(), originalUserDetails.getAuthorities());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(newUserDetails, newUserDetails
                .getPassword(), newUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
