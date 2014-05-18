package com.lingualearna.web.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lingualearna.web.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class JpaUserDetailsServiceTest {

    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    @Mock
    private UserService userService;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private User userEntity;

    private UserDetails userDetails;

    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService = new JpaUserDetailsService();

    private void givenTheUserDoesNotExist() {

        when(userService.getUserByUsername(USERNAME)).thenReturn(null);
    }

    private void givenTheUserExists() {

        when(userEntity.getUsername()).thenReturn(EMAIL_ADDRESS);
        when(userEntity.getPassword()).thenReturn(PASSWORD);
        when(userEntity.getRole()).thenReturn(Role.ROLE_ADMIN);
        when(userEntity.getEnabled()).thenReturn(true);
        when(userService.getUserByUsername(USERNAME)).thenReturn(userEntity);
    }

    private void givenTheUserIsDisabled() {

        when(userEntity.getEnabled()).thenReturn(false);
    }

    @Test
    public void testLoadUserByUsernameLoadsUser() {

        givenTheUserExists();
        whenICallLoadUserByUsername();
        thenTheUserIsLoaded();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameThrowsErrorOnNonExistentUser() {

        givenTheUserDoesNotExist();
        whenICallLoadUserByUsernameThenIGetAnException();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameThrowsErrorOnUserDisabled() {

        givenTheUserIsDisabled();
        whenICallLoadUserByUsernameThenIGetAnException();
    }

    private void thenTheUserIsLoaded() {

        assertEquals(EMAIL_ADDRESS, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        assertEquals(Role.ROLE_ADMIN.toString(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    private void whenICallLoadUserByUsername() {

        userDetails = jpaUserDetailsService.loadUserByUsername(USERNAME);
    }

    private void whenICallLoadUserByUsernameThenIGetAnException() {

        whenICallLoadUserByUsername();
    }
}
