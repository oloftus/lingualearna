package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class GenericDao<T> {

    @PersistenceContext
    private EntityManager em;
    private Class<T> entityType;

    /**
     * Workaround for compile time/runtime nature of Java generics.<br/>
     * <br/>
     * <b>This method must be called before usingthis class.</b>
     */
    public void setEntityType(Class<T> type) {

        this.entityType = type;
    }

    /**
     * Parameter obj is updated with any changes made by the database
     */
    public void persist(T obj) {

        em.persist(obj);
        em.flush();
    }

    public T findNoLock(int id) {

        T retrievedObj = em.find(entityType, id);
        return retrievedObj;
    }

    public T merge(T obj) {

        T updatedObj = em.merge(obj);
        return updatedObj;
    }

    /**
     * @return Whether the object to delete actually existed
     */
    public boolean delete(int id) {

        T retrievedObj = em.find(entityType, id);
        if (retrievedObj == null) {
            return false;
        }

        em.remove(findNoLock(id));

        return true;
    }
}
