package com.halosky.cosumer.proxy;

import com.halosky.cosumer.handler.RpcCosumerHandler;
import com.halosky.protocol.HaloProtocol;
import com.halosky.registy.HaloRegistyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcCosumerProxy {

    public static <T> T create(Class<?> clazz) {
        ConsumerProxy consumerProxy = new ConsumerProxy(clazz);
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, consumerProxy);
        return result;
    }


    private static class ConsumerProxy implements InvocationHandler {

        private Class<?> aClass;

        public ConsumerProxy(Class<?> aClass) {
            this.aClass = aClass;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(method.getDeclaringClass().equals(Object.class)){
                return method.invoke(this,args);
            }
            return rpcInvoke(proxy,method,args);
        }

        public Object rpcInvoke(Object proxy, Method method, Object[] args){
            HaloProtocol haloProtocol = new HaloProtocol();
            haloProtocol.setClassName(aClass.getName());
            haloProtocol.setMethodName(method.getName());
            haloProtocol.setArgs(method.getParameterTypes());
            haloProtocol.setValues(args);

            // 发起网络请求
            EventLoopGroup workGroup = new NioEventLoopGroup();

            Bootstrap client = new Bootstrap();

            final RpcCosumerHandler rpcCosumerHandler = new RpcCosumerHandler();

            client.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer < SocketChannel>(){

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
                    channelPipeline.addLast(rpcCosumerHandler);
                }
            });
            try {
                ChannelFuture channelFuture = client.connect("localhost",8080).sync();
                channelFuture.channel().writeAndFlush(haloProtocol);
                channelFuture.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }

            return rpcCosumerHandler.getResponse();
        }

    }

}
