package com.lingualearna.web.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;

import java.lang.reflect.Method;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Sets;
import com.lingualearna.web.dao.GenericDao;

@RunWith(MockitoJUnitRunner.class)
public class OwnerBasedVoterTest {

    private class OwnerBasedVoterTestHelper {

        @SecuredType(HasOwner.class)
        public void securedMethod() {

        }
    }

    private static final int OWNED_OBJECT_ID = 1;
    private static final String OWNER_USERNAME = "ownerUsername";
    private static final String OTHER_USERNAME = "otherUsername";
    private static final String TEST_METHOD_NAME = "securedMethod";

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private MethodInvocation methodInvocation;

    @Mock
    private ConfigAttribute configAttribute;

    @Mock
    private HasOwner ownedObject;

    @Mock
    private GenericDao<?> genericDao;

    private int actualVote;

    private Collection<ConfigAttribute> configAttributes;

    @InjectMocks
    private OwnerBasedVoter voter = new OwnerBasedVoter();

    private void andAccessIsDenied() {

        when(ownedObject.getOwnerUsername()).thenReturn(OWNER_USERNAME);
        when(userDetails.getUsername()).thenReturn(OTHER_USERNAME);
    }

    private void andAccessIsGranted() {

        when(ownedObject.getOwnerUsername()).thenReturn(OWNER_USERNAME);
        when(userDetails.getUsername()).thenReturn(OWNER_USERNAME);
    }

    private void givenTheOwnedObjectIsGivenById() throws Exception {

        Integer[] ownedObjectIds = new Integer[] { OWNED_OBJECT_ID };
        when(methodInvocation.getArguments()).thenReturn(ownedObjectIds);

        Method method = OwnerBasedVoterTestHelper.class.getMethod(TEST_METHOD_NAME);
        when(methodInvocation.getMethod()).thenReturn(method);
    }

    private void givenTheOwnedObjectIsGivenByType() {

        HasOwner[] ownedObjects = new HasOwner[] { ownedObject };
        when(methodInvocation.getArguments()).thenReturn(ownedObjects);
    }

    @Before
    public void setup() {

        configAttributes = Sets.newHashSet(configAttribute);
        when(configAttribute.getAttribute()).thenReturn(SecuredConfigAttributes.ALLOW_OWNER);
        when(genericDao.findNoLock(HasOwner.class, OWNED_OBJECT_ID)).thenReturn(ownedObject);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void testDeniesAccessIfOwnerDoesntMatchPrincipalById() throws Exception {

        givenTheOwnedObjectIsGivenById();
        andAccessIsDenied();
        whenIRequestAVote();
        thenAccessIsDenied();
    }

    @Test
    public void testDeniesAccessIfOwnerDoesntMatchPrincipalByType() {

        givenTheOwnedObjectIsGivenByType();
        andAccessIsDenied();
        whenIRequestAVote();
        thenAccessIsDenied();
    }

    @Test
    public void testGrantsAccessIfOwnerMatchesPrincipalByType() {

        givenTheOwnedObjectIsGivenByType();
        andAccessIsGranted();
        whenIRequestAVote();
        thenAccessIsGranted();
    }

    @Test
    public void testGrantsAccessIfOwnerMatchesPrincipalByTypeById() throws Exception {

        givenTheOwnedObjectIsGivenById();
        andAccessIsGranted();
        whenIRequestAVote();
        thenAccessIsGranted();
    }

    private void thenAccessIsDenied() {

        assertEquals(ACCESS_DENIED, actualVote);
    }

    private void thenAccessIsGranted() {

        assertEquals(ACCESS_GRANTED, actualVote);
    }

    private void whenIRequestAVote() {

        actualVote = voter.vote(authentication, methodInvocation, configAttributes);
    }
}
