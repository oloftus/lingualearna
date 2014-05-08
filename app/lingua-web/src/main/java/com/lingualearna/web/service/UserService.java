package com.lingualearna.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.UserDao;
import com.lingualearna.web.security.User;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> getAllUsers() {

        return userDao.getAllUsers();
    }

    public User getUserByUsername(String username) {

        return userDao.findByUsername(username);
    }
}
