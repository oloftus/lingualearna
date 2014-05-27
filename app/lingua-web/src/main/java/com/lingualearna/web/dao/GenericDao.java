package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenericDao extends AbstractDao {

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
