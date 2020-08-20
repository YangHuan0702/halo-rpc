package com.halosky.server;

import com.halosky.server.service.RpcProxyServer;

public class App {

    public static void main(String[] args) {
        RpcProxyServer proxyServer = new RpcProxyServer();
        proxyServer.publisher(8080);


    }
}
