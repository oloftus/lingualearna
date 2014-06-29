package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class GenericDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }
}
