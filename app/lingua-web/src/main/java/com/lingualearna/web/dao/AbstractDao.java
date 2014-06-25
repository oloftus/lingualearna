package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractDao {

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

    @SafeVarargs
    protected final <T> T doQueryWithParams(String namedQueryName, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        List<T> results = doQueryAsListWithParams(namedQueryName, clazz, params);

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);

    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> doUntypedQueryAsListWithParams(String namedQueryName,
            Pair<String, ? extends Object>... params) {

        Query query = entityManager.createNamedQuery(namedQueryName);

        for (Pair<String, ? extends Object> param : params) {
            query.setParameter(param.getLeft(), param.getRight());
        }

        List<T> results = query.getResultList();
        return results;
    }

    @SafeVarargs
    protected final <T> T doUntypedQueryWithParams(String namedQueryName, Pair<String, ? extends Object>... params) {

        List<T> results = doUntypedQueryAsListWithParams(namedQueryName, params);

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);
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
