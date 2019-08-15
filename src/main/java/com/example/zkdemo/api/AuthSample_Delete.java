package com.example.zkdemo.api;

import org.apache.zookeeper.*;

import java.nio.file.PathMatcher;

public class AuthSample_Delete implements Watcher {

    static final String PATH = "/zk-book-auth_test";
    static final String PATH2 = "/zk-book-auth_test/child";

    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 50000, new AuthSample_Delete());
        zk.addAuthInfo("digest", "foo:true".getBytes());
        zk.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zk.create(PATH2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);

        ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 50000, new AuthSample_Delete());
        try {
            zk2.delete(PATH2, -1);
        } catch (Exception e) {
            System.out.println("删除节点失败：" +e.getMessage());
        }

        ZooKeeper zk3 = new ZooKeeper("127.0.0.1:2181", 50000, new AuthSample_Delete());
        zk3.addAuthInfo("digest", "foo:true".getBytes());
        zk3.delete(PATH2, -1);
        System.out.println("成功删除节点：" + PATH2);

        ZooKeeper zk4 = new ZooKeeper("127.0.0.1:2181", 50000, new AuthSample_Delete());
        zk4.delete(PATH, -1);
        System.out.println("成功删除节点：" + PATH);
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
