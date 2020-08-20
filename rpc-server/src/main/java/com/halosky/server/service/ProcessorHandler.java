package com.halosky.server.service;

import com.halosky.common.request.RpcRequest;
import com.sun.xml.internal.ws.encoding.MtomCodec;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ProcessorHandler implements Runnable {

    private Socket socket;

    public ProcessorHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * 应该处理我们对应的流信息
     */
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 获取请求对象
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 调用方法
            Object invokeResult = invoke(rpcRequest);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(invokeResult);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != objectInputStream){
                    objectInputStream.close();
                }
                if(null != objectOutputStream){
                    objectOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用
     *
     * @param r
     */
    private Object invoke(RpcRequest r) throws Exception {
        Object[] fields = r.getFields();
        Class<?>[] fieldsArray = null;
        if(null != fields && fields.length > 0){
            fieldsArray = new Class[fields.length];
            for (int index = 0; index < fields.length; index++) {
                fieldsArray[index] = fields[index].getClass();
            }
        }
        Class clazz = Class.forName(r.getClassName());
        Method method = clazz.getMethod(r.getMethodName(), fieldsArray);
        Object result = method.invoke(clazz.newInstance(), fields);
        return result;
    }

}
