package com.lingualearna.web.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.lingualearna.web.security.User;

@Component
public class UserDao extends GenericDao<User> {

    /**
     * @return Returns null if a user with such a username does not exist
     */
    public User findByUsername(String username) {

        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);
        User result = null;

        try {
            result = query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }

        return result;
    }

    public List<User> getAllUsers() {

        return doQueryAsList(getEntityManager(), "User.findAll", User.class);
    }

    @PostConstruct
    public void init() {

        setEntityType(User.class);
    }
}
