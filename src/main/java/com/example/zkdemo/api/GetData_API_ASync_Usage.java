package com.example.zkdemo.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class GetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new GetData_API_ASync_Usage());
        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.getData(path, true, new IDataCallback(), null);
        zk.setData(path, "123".getBytes(), -1);

        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }else if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                zk.getData(watchedEvent.getPath(), true, new IDataCallback(), null);
            }
        }
    }
}
class IDataCallback implements AsyncCallback.DataCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
        System.out.println(rc + "," + path + "," +new String(bytes));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() +"," +stat.getVersion());
    }
}