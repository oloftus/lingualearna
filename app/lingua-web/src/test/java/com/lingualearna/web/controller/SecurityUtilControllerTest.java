package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SecurityUtilControllerTest {

    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String CORRECT_SECRET = "d7c6bdcbf60516f0a8a8d4c24a653149983c1320";
    private static final String INCORRECT_SECRET = "incorrectSecret";
    private static final String CSRF_TOKEN = "csrfToken";
    private static final String CSRF_TOKEN_ATTR = "_csrf";
    private static final String CSRF_SALT = "123456";
    private static final String CSRF_SALT_FIELD = "csrfSalt";

    @Mock
    private UsernamePasswordAuthenticationToken principal;

    @Mock
    private UserDetails userDetails;

    @Mock
    private CsrfToken csrfToken;

    @Mock
    private HttpServletRequest request;

    private String returnedCsrfToken;

    @InjectMocks
    private SecurityUtilController controller = new SecurityUtilController();

    @Before
    public void setup() {

        ReflectionTestUtils.setField(controller, CSRF_SALT_FIELD, CSRF_SALT);

        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userDetails.getPassword()).thenReturn(PASSWORD);
        when(principal.getPrincipal()).thenReturn(userDetails);

        when(csrfToken.getToken()).thenReturn(CSRF_TOKEN);
        when(request.getAttribute(CSRF_TOKEN_ATTR)).thenReturn(csrfToken);
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetCsrfTokenChecksSecret() {

        whenICallGetCsrfTokenWithAnIncorrectSecretThenAnExceptionIsThrown();
    }

    @Test
    public void testGetCsrfTokenReturnsCsrfToken() {

        whenICallGetCsrfTokenWithTheCorrectSecret();
        thenTheCorrectCsrfTokenIsReturned();
    }

    private void thenTheCorrectCsrfTokenIsReturned() {

        assertEquals(CSRF_TOKEN, returnedCsrfToken);
    }

    private void whenICallGetCsrfTokenWithAnIncorrectSecretThenAnExceptionIsThrown() {

        controller.getCsrfToken(request, principal, INCORRECT_SECRET);
    }

    private void whenICallGetCsrfTokenWithTheCorrectSecret() {

        returnedCsrfToken = controller.getCsrfToken(request, principal, CORRECT_SECRET);
    }
}
