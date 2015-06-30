package com.lingualearna.web.security.emailvalidation;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.shared.exceptions.UnexpectedProblemException;

public interface ValidationEmailSender {

    void send(AppUser user) throws UnexpectedProblemException;
}
