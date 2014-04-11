package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.lingualearna.web.util.ConfigurationException;

@Component
public class GenericDao<T> {

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entityType;

    private void checkClassIsSetup() {

        if (entityType == null) {
            throw new ConfigurationException("entityType field not set on " + this.getClass().toString() + " class");
        }
    }

    /**
     * @return Whether the object to delete actually existed
     */
    public boolean delete(int id) {

        checkClassIsSetup();
        T retrievedObj = entityManager.find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        entityManager.remove(retrievedObj);
        entityManager.flush();

        return true;
    }

    public T findNoLock(int id) {

        checkClassIsSetup();
        T retrievedObj = entityManager.find(entityType, id);
        return retrievedObj;
    }

    public T merge(T obj) {

        checkClassIsSetup();
        T updatedObj = entityManager.merge(obj);
        entityManager.flush();
        return updatedObj;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public void persist(T obj) {

        checkClassIsSetup();
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
