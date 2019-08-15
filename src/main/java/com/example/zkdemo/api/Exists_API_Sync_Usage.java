package com.example.zkdemo.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class Exists_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new Exists_API_Sync_Usage());
        connectedSemaphore.await();
        zk.exists(path, true);

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zk.setData(path, "456".getBytes(), -1);

        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zk.delete(path + "/c1", -1);
        zk.delete(path, -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    connectedSemaphore.countDown();
                }else if(Event.EventType.NodeCreated == watchedEvent.getType()){
                    System.out.println("Node(" +watchedEvent.getPath() + ")Created");
                    zk.exists(watchedEvent.getPath(), true);
                }else if(Event.EventType.NodeDeleted == watchedEvent.getType()){
                    System.out.println("Node(" +watchedEvent.getPath() + ")Deleted");
                    zk.exists(watchedEvent.getPath(), true);
                }else if(Event.EventType.NodeDataChanged == watchedEvent.getType()){
                    System.out.println("Node(" +watchedEvent.getPath() + ")DataChanged");
                    zk.exists(watchedEvent.getPath(), true);
                }


            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
