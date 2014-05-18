package com.lingualearna.web.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User implements Serializable {

    private static final long serialVersionUID = -9112339745471679923L;

    private int userId;
    private String username;
    private boolean enabled;
    private String password;
    private Role role;

    public boolean getEnabled() {

        return this.enabled;
    }

    @Length(max = 45)
    public String getPassword() {

        return this.password;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    public Role getRole() {

        return this.role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    public int getUserId() {

        return this.userId;
    }

    @Column(name = "email_address")
    @Email
    public String getUsername() {

        return this.username;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
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

    public void setUsername(String username) {

        this.username = username;
    }
}
