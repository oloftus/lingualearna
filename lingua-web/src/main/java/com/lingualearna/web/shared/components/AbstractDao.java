package com.lingualearna.web.shared.components;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractDao {

    @SuppressWarnings("unchecked")
    private Query createQueryWithParams(String namedQueryName, Pair<String, ? extends Object>... params) {

        Query query = getEntityManager().createNamedQuery(namedQueryName);

        for (Pair<String, ? extends Object> param : params) {
            query.setParameter(param.getLeft(), param.getRight());
        }
        return query;
    }

    /**
     * @return Whether the object to delete actually exists
     */
    public <T> boolean delete(Class<T> entityType, int id) {

        T retrievedObj = getEntityManager().find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        getEntityManager().remove(retrievedObj);

        return true;
    }

    @SafeVarargs
    public final void doExecuteUpdateWithParams(String namedQueryName, Pair<String, ? extends Object>... params) {

        Query query = createQueryWithParams(namedQueryName, params);
        query.executeUpdate();
        getEntityManager().clear();
    }

    public <T> List<T> doQueryAsList(String queryStr, Class<T> clazz) {

        TypedQuery<T> query = getEntityManager().createNamedQuery(queryStr, clazz);
        List<T> results = query.getResultList();
        return results;
    }

    /**
     * @param params
     *            (Name, Value) pairs of query parameters
     */
    @SafeVarargs
    public final <T> List<T> doQueryAsListWithParams(String namedQueryName, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        TypedQuery<T> query = doQueryAsQueryWithParams(namedQueryName, clazz, params);

        List<T> results = query.getResultList();
        return results;
    }

    @SafeVarargs
    public final <T> TypedQuery<T> doQueryAsQueryWithParams(String namedQueryName, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQueryName, clazz);

        for (Pair<String, ? extends Object> param : params) {
            query.setParameter(param.getLeft(), param.getRight());
        }
        return query;
    }

    @SafeVarargs
    public final <T> T doQueryWithParams(String namedQueryName, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        List<T> results = doQueryAsListWithParams(namedQueryName, clazz, params);

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);

    }

    @SuppressWarnings("unchecked")
    public <T> List<T> doUntypedQueryAsListWithParams(String namedQueryName,
            Pair<String, ? extends Object>... params) {

        Query query = createQueryWithParams(namedQueryName, params);

        List<T> results = query.getResultList();
        return results;
    }

    @SafeVarargs
    public final <T> T doUntypedQueryWithParams(String namedQueryName, Pair<String, ? extends Object>... params) {

        List<T> results = doUntypedQueryAsListWithParams(namedQueryName, params);

        if (results.size() == 0) {
            return null;
        }

        return results.get(0);
    }

    public <T> T find(Class<T> entityType, int id) {

        return getEntityManager().find(entityType, id);
    }

    protected abstract EntityManager getEntityManager();

    public <T> T merge(T obj) {

        T updatedObj = getEntityManager().merge(obj);
        return updatedObj;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public <T> void persist(T obj) {

        getEntityManager().persist(obj);
    }
}
