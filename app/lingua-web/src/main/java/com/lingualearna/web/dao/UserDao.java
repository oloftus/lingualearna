package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.lingualearna.web.security.User;

@Component
public class UserDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @return Returns null if a user with such a username does not exist
     */
    public User findByUsername(String username) {

        return doQueryWithParams(User.FIND_BY_USERNAME_QUERY, User.class, Pair.of(User.USERNAME_QUERY_PARAM, username));
    }

    public List<User> getAllUsers() {

        return doQueryAsList(User.FIND_ALL_QUERY, User.class);
    }

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }
}
