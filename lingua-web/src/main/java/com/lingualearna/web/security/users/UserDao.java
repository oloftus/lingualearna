package com.lingualearna.web.security.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class UserDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AppUser findByEmailValidationKey(String emailValidationKey) {

        return doQueryWithParams(AppUser.FIND_BY_EMAIL_VALIDATION_KEY_QUERY, AppUser.class, Pair.of(
                AppUser.EMAIL_VALIDATION_KEY_QUERY_PARAM, emailValidationKey));
    }

    /**
     * @return Returns null if a user with such a username does not exist
     */
    public AppUser findByUsername(String username) {

        return doQueryWithParams(AppUser.FIND_BY_USERNAME_QUERY, AppUser.class, Pair.of(
                AppUser.EMAIL_ADDRESS_QUERY_PARAM, username));
    }

    public List<AppUser> getAllUsers() {

        return doQueryAsList(AppUser.FIND_ALL_QUERY, AppUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public boolean userExists(String username) {

        return doQueryWithParams(AppUser.USER_EXISTS_QUERY, Boolean.class, Pair.of(AppUser.EMAIL_ADDRESS_QUERY_PARAM,
                username));
    }
}
