package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @return Whether the object to delete actually exists
     */
    public <T> boolean delete(Class<T> entityType, int id) {

        T retrievedObj = entityManager.find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        entityManager.remove(retrievedObj);
        entityManager.flush();

        return true;
    }

    protected <T> List<T> doQueryAsList(String queryStr, Class<T> clazz) {

        TypedQuery<T> query = entityManager.createNamedQuery(queryStr, clazz);
        List<T> results = query.getResultList();
        return results;
    }

    /**
     * @param params
     *            (Name, Value) pairs of query parameters
     */
    @SafeVarargs
    protected final <T> List<T> doQueryAsListWithParams(String namedQueryName, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        TypedQuery<T> query = entityManager.createNamedQuery(namedQueryName, clazz);

        for (Pair<String, ? extends Object> param : params) {
            query.setParameter(param.getLeft(), param.getRight());
        }

        List<T> results = query.getResultList();
        return results;
    }

    public <T> T find(Class<T> entityType, int id) {

        return entityManager.find(entityType, id);
    }

    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public <T> T merge(T obj) {

        T updatedObj = entityManager.merge(obj);
        entityManager.flush();
        return updatedObj;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public <T> void persist(T obj) {

        entityManager.persist(obj);
        entityManager.flush();
    }
}
