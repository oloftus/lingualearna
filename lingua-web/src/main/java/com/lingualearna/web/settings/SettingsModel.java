package com.lingualearna.web.settings;

import com.lingualearna.web.shared.validation.annotations.FieldsMatch;

@FieldsMatch.Fields({
        @FieldsMatch(value = { "password", "confirmPassword" }, message = "Passwords must match")
})
public class SettingsModel {

    public static final String PASSWORD_FIELD = "password";

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String confirmPassword;

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
