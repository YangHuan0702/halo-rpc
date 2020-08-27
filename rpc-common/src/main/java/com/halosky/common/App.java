package com.halosky.common;

import com.halosky.common.entity.User;

public class App {

    public static void main(String[] args) {
        User user = new User();
        try {
            user.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
