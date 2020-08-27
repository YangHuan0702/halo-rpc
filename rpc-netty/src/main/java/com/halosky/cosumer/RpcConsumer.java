package com.halosky.cosumer;

import com.halosky.api.IUserService;
import com.halosky.cosumer.proxy.RpcCosumerProxy;

public class RpcConsumer {

    public static void main(String[] args) {
        IUserService iUserService = RpcCosumerProxy.create(IUserService.class);
        System.out.println(iUserService.rememberMe("halosky"));
    }

}
