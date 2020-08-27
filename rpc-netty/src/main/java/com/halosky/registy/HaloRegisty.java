package com.halosky.registy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class HaloRegisty {

    private int port;

    // 启动方法
    public void start(){

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 主线程池初始化
        EventLoopGroup bossEvent = new NioEventLoopGroup();
        // 子线程初始化 具体对应客户端逻辑
        EventLoopGroup wokerEvent = new NioEventLoopGroup();

        serverBootstrap.group(bossEvent,wokerEvent).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            // 处理逻辑
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline channelPipeline = socketChannel.pipeline();

                // 对于自定义协议的内容进行解码
                channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                        0,4,0,4));

                // 自动编码
                channelPipeline.addLast(new LengthFieldPrepender(4));


                // 实参的处理
                channelPipeline.addLast("encoder",new ObjectEncoder());
                channelPipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                // 编码/解码就是对数据进行解析
                // 执行我们自己的逻辑
                // 1.注册 给每个对象起一个名称。对外提供服务的名称
                // 2.服务位置要做一个登记
                channelPipeline.addLast(new HaloRegistyHandler());

            }
        }).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);

        try {
            // 正式启动服务 相当于用一个死循环在轮询
            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            System.out.println("SERVER START LISTENN AT "+ this.port);
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new HaloRegisty(8080).start();
    }

    public HaloRegisty(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
