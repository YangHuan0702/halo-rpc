package com.halosky.server;

import com.halosky.server.config.ServerConfig;
import com.halosky.server.service.RpcProxyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
    //    RpcProxyServer proxyServer = new RpcProxyServer();
    //    proxyServer.publisher(8080);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerConfig.class);
        ((AnnotationConfigApplicationContext) applicationContext).start();
    }
}
