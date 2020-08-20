package com.halosky.client;

import com.halosky.client.connection.RpcConnectionTransport;
import com.halosky.client.proxy.RpcProxyClient;
import com.halosky.common.request.RpcRequest;
import com.halosky.common.service.UserService;
import com.halosky.common.service.UserServiceImpl;

public class App {

    public static void main(String[] args) {

//        UserService userService = RpcProxyClient.clientProxy(UserService.class,"localhost",8080);
//        userService.getUser();

        RpcConnectionTransport rpcConnectionTransport = new RpcConnectionTransport("localhost",8080);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setFields(null);
        rpcRequest.setClassName("com.halosky.common.service.UserServiceImpl");
        rpcRequest.setMethodName("getUser");
        rpcConnectionTransport.send(rpcRequest);
    }

}
