package com.halosky.server.config;

import com.halosky.server.service.RpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.halosky.server")
public class ServerConfig {

    @Bean(name = "rpcServcer")
    public RpcServer rpcServer(){
        return new RpcServer(8080);
    }

}
