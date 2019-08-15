package com.example.zkdemo.api;

import org.apache.zookeeper.*;

public class AuthSample_Get2 implements Watcher {

    static final String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, new AuthSample_Get2());
        zk.addAuthInfo("digest", "foo:true".getBytes());
        zk.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 5000, new AuthSample_Get2());
        zk2.addAuthInfo("digest", "foo:true".getBytes());//正确的权限信息
        System.out.println("=================>" + new String(zk2.getData(PATH, false, null)));

        ZooKeeper zk3 = new ZooKeeper("127.0.0.1:2181", 5000, new AuthSample_Get2());
       ;zk3.addAuthInfo("digest", "foo:false".getBytes());//错误的权限信息
        zk3.getData(PATH, false, null);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                System.out.println("connected ...");
            }
        }
    }
}
