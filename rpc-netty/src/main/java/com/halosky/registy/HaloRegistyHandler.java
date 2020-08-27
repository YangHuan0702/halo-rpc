package com.halosky.registy;

import com.halosky.protocol.HaloProtocol;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaloRegistyHandler extends ChannelInboundHandlerAdapter {

    private List<String> classList = new ArrayList<String>();

    private Map<String,Object> registryMap = new HashMap<String, Object>();

    public HaloRegistyHandler(){
        scannerClass("com.halosky.provider");

        doRegistry();
    }

    public void doRegistry(){
        if(classList.size() > 0){
            for(String className : classList){
                try {
                    Class<?> clazz = Class.forName(className);
                    Class<?> i = clazz.getInterfaces()[0];
                    String serviceName = i.getName();
                    registryMap.put(serviceName,clazz.newInstance());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    // 扫描class
    public void scannerClass(String s){
        URL url = this.getClass().getClassLoader().getResource(s.replaceAll("\\.","/"));
        File file = new File(url.getFile());
        for(File fs : file.listFiles()){
            if(fs.isDirectory()){
                scannerClass(s+"."+fs.getName());
            }else{
                classList.add(s+"."+fs.getName().replace(".class",""));
            }
        }
    }

    // 1.根据包名将所有符合条件的class 全部扫描出来，存在一个容器中
    // 2.给每个对应的class取一个唯一的名称作为服务名称，保存在一个容器中
    // 3.当有客户端连接过来之后，就会获取协议内容 ：HaloProtocol对象
    // 4.要去注册好的容器当中找到对应的服务
    // 5.通过远程调用Provider得到返回结果，并回复给客户端
    // 如果是分布式环境，就读配置文件
    // 客户端连接上了之后的回调方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HaloProtocol haloProtocol = (HaloProtocol)msg;

        Object service = registryMap.get(haloProtocol.getClassName());

        Method method = service.getClass().getMethod(haloProtocol.getMethodName(),haloProtocol.getArgs());
        Object result = method.invoke(service,haloProtocol.getValues());

        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    //连接发生异常的时候的回调方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
