package com.halosky.client.proxy;

import java.lang.reflect.Proxy;

public class RpcProxyClient {

    public static  <T> T clientProxy (final Class<T> targetClass,final String host,final int port){
        return (T)Proxy.newProxyInstance(targetClass.getClassLoader(),new Class<?>[]{targetClass},
                new RemoteInvocationHandler(host,port));
    }

}
