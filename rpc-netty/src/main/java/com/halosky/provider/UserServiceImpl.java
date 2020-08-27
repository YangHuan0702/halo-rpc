package com.halosky.provider;

import com.halosky.api.IUserService;


public class UserServiceImpl implements IUserService {

    public String rememberMe(String name) {
        return "remember " + name;
    }
}
