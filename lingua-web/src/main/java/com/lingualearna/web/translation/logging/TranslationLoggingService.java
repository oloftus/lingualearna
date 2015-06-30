package com.lingualearna.web.translation.logging;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.data.GenericDao;
import com.lingualearna.web.translation.TranslationProviderName;

@Service
@Transactional
public class TranslationLoggingService {

    @Autowired
    private UserService userService;

    @Autowired
    private GenericDao dao;

    public void logTranslationRequest(TranslationProviderName provider) {

        AppUser user = getCurrentUser();
        LogEntry logEntry = new LogEntry(user.getUserId(), user.getEmailAddress(), DateTime.now(), provider);
        dao.persist(logEntry);
    }

    private AppUser getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getUserByUsername(userDetails.getUsername());
    }
}
