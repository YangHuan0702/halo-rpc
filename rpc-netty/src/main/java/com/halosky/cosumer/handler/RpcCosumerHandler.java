package com.halosky.cosumer.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcCosumerHandler extends ChannelInboundHandlerAdapter {

    private Object response;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
    }


    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

    }

    public Object getResponse(){
        return response;
    }

}
