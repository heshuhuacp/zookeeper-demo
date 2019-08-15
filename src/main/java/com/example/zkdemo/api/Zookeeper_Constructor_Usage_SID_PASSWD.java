package com.example.zkdemo.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class Zookeeper_Constructor_Usage_SID_PASSWD implements Watcher {

    public static CountDownLatch  downLatch = new CountDownLatch(1);


    public static void main(String[] args) throws Exception{

        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000
                , new Zookeeper_Constructor_Usage_SID_PASSWD());

        downLatch.await();
        long sessionId = zookeeper.getSessionId();
        byte[] passwd = zookeeper.getSessionPasswd();

        zookeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new Zookeeper_Constructor_Usage_SID_PASSWD(),
                1l, "test".getBytes());

        zookeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new Zookeeper_Constructor_Usage_SID_PASSWD(),
                sessionId, passwd);

        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("Received watched event:" +watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            downLatch.countDown();
        }
    }
}
