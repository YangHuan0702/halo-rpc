package com.halosky.server.service;

import com.halosky.common.entity.User;
import com.halosky.common.service.UserService;
import com.halosky.server.annotation.RpcService;

@RpcService("com.halosky.common.service.UserService")
public class UserServiceImpl implements UserService {

    public User getUser() {
        User user = new User();
        user.setAge("18");
        user.setPassword("123456");
        user.setUserName("halosku");
        return user;
    }
}
