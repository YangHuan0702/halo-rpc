package com.halosky.client;

import com.halosky.client.connection.RpcConnectionTransport;
import com.halosky.client.proxy.RpcProxyClient;
import com.halosky.common.request.RpcRequest;
import com.halosky.common.service.UserService;

public class App {

    public static void main(String[] args) {

        UserService userService = RpcProxyClient.clientProxy(UserService.class,"localhost",8080);
        userService.getUser();
    }

}
