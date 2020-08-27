package com.halosky.zk.connection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZkConnection {

    public static void main(String[] args) {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.
                builder().connectString("localhost:2181").
                sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

        curatorFramework.start();

        try {
//            create(curatorFramework);
            chlidChangeListener(curatorFramework);
            onChildChange(curatorFramework);
            setNodel(curatorFramework);

            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void onChildChange(CuratorFramework curatorFramework)throws Exception{
       NodeCache nodeCache = new NodeCache(curatorFramework,"/wendy",false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("进入监听");
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }


    public static void chlidChangeListener(CuratorFramework curatorFramework) throws Exception{
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/wendy",false);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println(pathChildrenCacheEvent.getType());
                System.out.println("PATH:"+pathChildrenCacheEvent.getData().getPath());
                System.out.println("data:"+new String(pathChildrenCacheEvent.getData().getData()));

            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();

    }


    public static void setNodel(CuratorFramework curatorFramework)throws Exception{
        String val = new String(curatorFramework.getData().forPath("/wendy"));
        System.out.println(val);
        curatorFramework.setData().forPath("/wendy","123".getBytes());
    }

    public static void delete(CuratorFramework curatorFramework)throws Exception{
        curatorFramework.delete().forPath("/wendy");
    }

    public static void create( CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/wendy");
    }



}
