package com.halosky.client.proxy;

import com.halosky.client.connection.RpcConnectionTransport;
import com.halosky.common.request.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvocationHandler implements InvocationHandler {

    private String ip;
    private int port;

    public RemoteInvocationHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setFields(args);

        RpcConnectionTransport rpcConnectionTransport = new RpcConnectionTransport(ip,port);
        rpcConnectionTransport.send(rpcRequest);
        return rpcConnectionTransport.send(rpcRequest);
    }
}
