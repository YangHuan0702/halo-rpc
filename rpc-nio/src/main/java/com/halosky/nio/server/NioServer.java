package com.halosky.nio.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private int port;

    // 轮询器 Selector
    // 缓冲区 Buffer

    private Selector selector;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(10);


    public NioServer(int port) {
        this.port = port;
        try {
            ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();

            serverSocketChannel.bind(new InetSocketAddress("localhost",port));

            // 设置非阻塞 default = true
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();

            // 初始化
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public int getPort() {
        return port;
    }

    public void listen()throws Exception{
        System.out.println(" in listen");
        while (true){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = keys.iterator();
            // 不断迭代，轮询
            // 在这就体现出同步了，因为每次只能拿到一个Key，每次只能处理一种状态
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();

                process(selectionKey);
            }
        }
    }

    private void process(SelectionKey selectionKey)throws Exception{
        // 准备状态 -> 可读
        if(selectionKey.isAcceptable()){
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            // 当状态就就绪时，更改为可读
            selectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
        }
        else if(selectionKey.isReadable()){
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            int len = socketChannel.read(byteBuffer);
            if(len > 0){
                byteBuffer.flip();
                String context = new String(byteBuffer.array(),0,len,"UTF-8");
                selectionKey = socketChannel.register(selector,SelectionKey.OP_WRITE);
                selectionKey.attach(context);
                System.out.println("读取到内容:"+context);
            }
        }
        else if(selectionKey.isWritable()){
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            String context = (String)selectionKey.attachment();
            socketChannel.write(byteBuffer.wrap(("输出:"+context).getBytes()));
            socketChannel.close();
        }
    }

    public static void main(String[] args) {
        try {
            new NioServer(8080).listen();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setPort(int port) {
        this.port = port;
    }
}
