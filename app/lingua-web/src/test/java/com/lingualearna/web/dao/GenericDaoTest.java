package com.lingualearna.web.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericDaoTest {

    private static final Class<Object> DAO_GENERIC_TYPE = Object.class;
    private static final int INVALID_ID = 2;
    private static final int OBJ_ID = 1;
    private static final int VALID_ID = 1;

    private boolean deleteSuccess;
    private Object actualResult;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Object inputObject;

    @Mock
    private Object entitytManagerResult;

    @InjectMocks
    private final AbstractDao dao = new GenericDao();

    private void givenEntityManagerFindIsSetup() {

        when(entityManager.find(DAO_GENERIC_TYPE, INVALID_ID)).thenReturn(null);
        when(entityManager.find(DAO_GENERIC_TYPE, VALID_ID)).thenReturn(entitytManagerResult);
    }

    private void givenEntityManagerMergeIsSetup() {

        when(entityManager.merge(inputObject)).thenReturn(entitytManagerResult);
    }

    @After
    public void teardown() {

        actualResult = null;
    }

    @Test
    public void testDeleteFunctions() {

        givenEntityManagerFindIsSetup();
        whenICallDeleteWithAValidId();
        thenTheObjectIsRemoved();
    }

    @Test
    public void testDeleteReturnsFalseOnInvalidId() {

        whenICallDeleteWithAnInvalidId();
        thenFalseIsReturned();
    }

    @Test
    public void testFindFunctions() {

        givenEntityManagerFindIsSetup();
        whenICallFindWithAValidId();
        thenTheTheObjectIsFound();
    }

    @Test
    public void testMergeFunctions() {

        givenEntityManagerMergeIsSetup();
        whenICallMergeWithAValidId();
        thenTheObjectIsMerged();
    }

    @Test
    public void testPersistFunctions() {

        whenICallPersistWithAValidId();
        thenTheObjectIsPersisted();
    }

    private void thenFalseIsReturned() {

        assertFalse(deleteSuccess);
    }

    private void thenTheObjectIsMerged() {

        assertEquals(entitytManagerResult, actualResult);
        verify(entityManager).flush();
    }

    private void thenTheObjectIsPersisted() {

        verify(entityManager).persist(inputObject);
        verify(entityManager).flush();
    }

    private void thenTheObjectIsRemoved() {

        assertTrue(deleteSuccess);
        verify(entityManager).remove(entitytManagerResult);
        verify(entityManager).flush();
    }

    private void thenTheTheObjectIsFound() {

        assertEquals(entitytManagerResult, actualResult);
    }

    private void whenICallDeleteWithAnInvalidId() {

        deleteSuccess = dao.delete(Object.class, INVALID_ID);
    }

    private void whenICallDeleteWithAValidId() {

        deleteSuccess = dao.delete(Object.class, VALID_ID);
    }

    private void whenICallFindWithAValidId() {

        actualResult = dao.find(Object.class, VALID_ID);
    }

    private void whenICallMergeWithAValidId() {

        actualResult = dao.merge(inputObject);
    }

    private void whenICallPersistWithAValidId() {

        dao.persist(inputObject);
    }
}
