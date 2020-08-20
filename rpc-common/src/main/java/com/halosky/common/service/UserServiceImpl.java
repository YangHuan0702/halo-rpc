package com.halosky.common.service;

import com.halosky.common.entity.User;
import com.halosky.common.service.UserService;

public class UserServiceImpl implements UserService {

    public User getUser() {
        User user = new User();
        user.setAge("18");
        user.setPassword("123456");
        user.setUserName("halosku");
        return user;
    }
}
