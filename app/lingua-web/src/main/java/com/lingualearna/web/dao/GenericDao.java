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

    private void isClassSetup() {

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

        isClassSetup();
        T retrievedObj = entityManager.find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        entityManager.remove(retrievedObj);
        entityManager.flush();

        return true;
    }

    public T findNoLock(int id) {

        isClassSetup();
        T retrievedObj = entityManager.find(entityType, id);
        return retrievedObj;
    }

    public T merge(T obj) {

        isClassSetup();
        T updatedObj = entityManager.merge(obj);
        entityManager.flush();
        return updatedObj;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public void persist(T obj) {

        isClassSetup();
        entityManager.persist(obj);
        entityManager.flush();
    }

    /**
     * Workaround for compile time/runtime nature of Java generics.<br/>
     * <br/>
     * <b>This method must be called before usingthis class.</b>
     */
    public void setEntityType(Class<T> type) {

        this.entityType = type;
    }
}