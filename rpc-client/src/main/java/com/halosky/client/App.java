package com.halosky.client;


import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
//
//        UserService userService = RpcProxyClient.clientProxy(UserService.class,"localhost",8080);
//        userService.getUser();

        try {
            Socket socket = new Socket("localhost", 8080);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            String uuid = UUID.randomUUID().toString();
            objectOutputStream.write(uuid.getBytes("UTF-8"));
            System.out.println("发送数据:" + uuid);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
