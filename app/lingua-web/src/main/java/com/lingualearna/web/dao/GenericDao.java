package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lingualearna.web.util.ConfigurationException;

@Component
public class GenericDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> entityType;

    private void assertClassSetup() {

        if (entityType == null) {
            String message = "entityType field not set in GenericDao";
            ConfigurationException exception = new ConfigurationException(message);
            LOG.error(message, exception);
            throw exception;
        }
    }

    /**
     * @return Whether the object to delete actually exists
     */
    public boolean delete(int id) {

        assertClassSetup();
        T retrievedObj = entityManager.find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        entityManager.remove(retrievedObj);
        entityManager.flush();

        return true;
    }

    public <S> S findNoLock(Class<S> entityType, int id) {

        return entityManager.find(entityType, id);
    }

    public T findNoLock(int id) {

        assertClassSetup();
        return findNoLock(entityType, id);
    }

    protected EntityManager getEntityManager() {

        return entityManager;
    }

    public T merge(T obj) {

        assertClassSetup();
        T updatedObj = entityManager.merge(obj);
        entityManager.flush();
        return updatedObj;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public void persist(T obj) {

        assertClassSetup();
        entityManager.persist(obj);
        entityManager.flush();
    }

    /**
     * Workaround for compile time/runtime nature of Java generics.<br/>
     * <br/>
     * <b>This method must be called before using this class.</b>
     */
    public void setEntityType(Class<T> type) {

        this.entityType = type;
    }
}
