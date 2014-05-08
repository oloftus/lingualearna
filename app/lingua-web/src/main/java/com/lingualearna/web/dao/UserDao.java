package com.lingualearna.web.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.lingualearna.web.security.User;

@Component
public class UserDao extends GenericDao<User> {

    private static final String FIND_ALL_USERS_QUERY = "User.findAll";
    private static final String FIND_USER_BY_USERNAME_QUERY = "User.findByUsername";
    private static final String USERNAME_PARAM = "username";

    /**
     * @return Returns null if a user with such a username does not exist
     */
    public User findByUsername(String username) {

        TypedQuery<User> query = getEntityManager().createNamedQuery(FIND_USER_BY_USERNAME_QUERY, User.class);
        query.setParameter(USERNAME_PARAM, username);
        User result = null;

        try {
            result = query.getSingleResult();
        }
        catch (NoResultException e) {
            // Just return null
        }
        return result;
    }

    public List<User> getAllUsers() {

        TypedQuery<User> query = getEntityManager().createNamedQuery(FIND_ALL_USERS_QUERY,
                User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @PostConstruct
    public void init() {

        setEntityType(User.class);
    }
}
