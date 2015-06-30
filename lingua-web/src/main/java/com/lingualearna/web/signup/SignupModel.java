package com.lingualearna.web.signup;

import com.lingualearna.web.shared.validation.annotations.FieldsMatch;

@FieldsMatch.Fields({
        @FieldsMatch(value = { "emailAddress", "confirmEmailAddress" }, message = "Email addresses must match"),
        @FieldsMatch(value = { "password", "confirmPassword" }, message = "Passwords must match")
})
public class SignupModel {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String confirmEmailAddress;
    private String password;
    private String confirmPassword;

    /**
     * Placeholder property to enable JSP/Spring error invalid Captcha response
     */
    public String getCaptchaPlaceholder() {

        return null;
    }

    public String getConfirmEmailAddress() {

        return confirmEmailAddress;
    }

    public String getConfirmPassword() {

        return confirmPassword;
    }

    public String getEmailAddress() {

        return emailAddress;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getPassword() {

        return password;
    }

    public void setConfirmEmailAddress(String confirmEmailAddress) {

        this.confirmEmailAddress = confirmEmailAddress;
    }

    public void setConfirmPassword(String confirmPassword) {

        this.confirmPassword = confirmPassword;
    }

    public void setEmailAddress(String emailAddress) {

        this.emailAddress = emailAddress;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
