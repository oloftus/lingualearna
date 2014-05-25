package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractDao {

    protected <T> List<T> doQueryAsList(EntityManager entityManager, String queryStr, Class<T> clazz) {

        TypedQuery<T> query = entityManager.createNamedQuery(queryStr, clazz);
        List<T> results = query.getResultList();
        return results;
    }

    /**
     * @param params
     *            (Name, Value) pairs of query parameters
     */
    @SafeVarargs
    protected final <T> List<T> doQueryAsListWithParams(EntityManager entityManager, String queryStr, Class<T> clazz,
            Pair<String, ? extends Object>... params) {

        TypedQuery<T> query = entityManager.createNamedQuery(queryStr, clazz);

        for (Pair<String, ? extends Object> param : params) {
            query.setParameter(param.getLeft(), param.getRight());
        }

        List<T> results = query.getResultList();
        return results;
    }
}
