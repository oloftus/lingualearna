package com.lingualearna.web.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.lingualearna.web.security.users.AppUser;

@Service
public class CsrfService {

    @Value("${security.csrfSalt}")
    private String csrfSalt;

    public void assertSecretCorrect(AppUser currentUser, String sentSecret) {

        String correctSecret = getCsrfSecret(currentUser);

        if (!sentSecret.equals(correctSecret)) {
            throw new AccessDeniedException("Incorrect secret");
        }
    }

    public String getCsrfSecret(AppUser currentUser) {

        return DigestUtils.sha1Hex(currentUser.getEmailAddress() + currentUser.getPassword() + csrfSalt);
    }
}
