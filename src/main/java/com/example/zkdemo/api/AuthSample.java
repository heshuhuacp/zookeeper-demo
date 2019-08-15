package com.example.zkdemo.api;

import org.apache.zookeeper.*;

public class AuthSample implements Watcher {

    static final String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 50000, new AuthSample());
        zk.addAuthInfo("digest", "foo:true".getBytes());
        zk.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

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
