package com.lingualearna.web.service;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.UserDao;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USERNAME = "username";

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsersCallsDao() {

        userService.getAllUsers();
        verify(userDao).getAllUsers();
    }

    @Test
    public void getUserByUsernameCallsDao() {

        userService.getUserByUsername(USERNAME);
        verify(userDao).findByUsername(USERNAME);
    }
}
