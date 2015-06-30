package com.lingualearna.web.security.users;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import com.lingualearna.web.pages.Page;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = AppUser.FIND_ALL_QUERY, query = "SELECT u FROM AppUser u"),
        @NamedQuery(name = AppUser.FIND_BY_USERNAME_QUERY, query = "SELECT u FROM AppUser u WHERE u.emailAddress = :emailAddress"),
        @NamedQuery(name = AppUser.FIND_BY_EMAIL_VALIDATION_KEY_QUERY, query = "SELECT u FROM AppUser u WHERE u.emailValidationKey = :emailValidationKey"),
        @NamedQuery(name = AppUser.USER_EXISTS_QUERY, query = "SELECT count(u) > 1 FROM AppUser u WHERE u.emailAddress = :emailAddress")
})
public class AppUser implements Serializable {

    private static final long serialVersionUID = -9112339745471679923L;

    public static final String FIND_ALL_QUERY = "User.findAll";
    public static final String USER_EXISTS_QUERY = "User.userExists";
    public static final String FIND_BY_USERNAME_QUERY = "User.findByUsername";
    public static final String FIND_BY_EMAIL_VALIDATION_KEY_QUERY = "User.findByEmailValidationKey";

    public static final String EMAIL_ADDRESS_QUERY_PARAM = "emailAddress";
    public static final String EMAIL_VALIDATION_KEY_QUERY_PARAM = "emailValidationKey";

    private int userId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String emailValidationKey;
    private boolean enabled;
    private String password;
    private Role role;
    private Page lastUsed;
    private DateTime dateTimeCreated;

    @Column(name = "created")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getDateTimeCreated() {

        return dateTimeCreated;
    }

    @Email
    @Length(max = 254)
    @NotBlank
    @Column(name = "email_address")
    public String getEmailAddress() {

        return this.emailAddress;
    }

    @Column(name = "email_key")
    public String getEmailValidationKey() {

        return emailValidationKey;
    }

    @Length(max = 45)
    @NotBlank
    @Column(name = "first_name")
    public String getFirstName() {

        return firstName;
    }

    @Length(max = 45)
    @NotBlank
    @Column(name = "last_name")
    public String getLastName() {

        return lastName;
    }

    @OneToOne
    @JoinColumn(name = "last_used")
    public Page getLastUsed() {

        return lastUsed;
    }

    @Column(name = "password")
    public String getPassword() {

        return this.password;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    public Role getRole() {

        return this.role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public int getUserId() {

        return this.userId;
    }

    @Column(name = "enabled")
    public boolean isEnabled() {

        return this.enabled;
    }

    public void setDateTimeCreated(DateTime dateTimeCreated) {

        this.dateTimeCreated = dateTimeCreated;
    }

    public void setEmailAddress(String emailAddress) {

        this.emailAddress = emailAddress;
    }

    public void setEmailValidationKey(String emailValidationKey) {

        this.emailValidationKey = emailValidationKey;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public void setLastUsed(Page lastUsed) {

        this.lastUsed = lastUsed;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }
}
