package com.halosky.server.service;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcProxyServer {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void publisher(int port) {
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                // 每一个socket都交给一个processorHandler来处理
                executorService.execute(new ProcessorHandler(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != serverSocket){
                try {
                    serverSocket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
