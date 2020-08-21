package com.halosky.server.service;

import com.halosky.common.service.UserService;
import com.halosky.server.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer implements ApplicationContextAware, InitializingBean {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private int port;

    private Map<String,Object> serviceHandler = new HashMap<String, Object>();

    public RpcServer(int port){
        this.port = port;
    }

    public void afterPropertiesSet() throws Exception {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                // 每一个socket都交给一个processorHandler来处理
                executorService.execute(new ProcessorHandler(socket,serviceHandler));
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

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> rpcServiceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        for(Map.Entry<String,Object> entry : rpcServiceMap.entrySet()){
            RpcService rpcService = entry.getValue().getClass().getAnnotation(RpcService.class);
            String className = rpcService.value();
            if(!StringUtils.isEmpty(className)){
                serviceHandler.put(className,entry.getValue());
            }
        }
    }
}
