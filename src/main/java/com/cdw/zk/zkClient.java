package com.cdw.zk;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author: cdw
 * @date: 2021/12/24 20:17
 * @description:
 */
public class zkClient {

    private String connectString = "47.108.192.114:2182";
    private int sessionTimeout = 50000;
    private ZooKeeper client;

    @Before
    public void init() throws IOException {
        client = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                List<String> children = null;
                try {
                    children = client.getChildren("/", true);
                    System.out.println("========================");
                    for (String child : children)
                        System.out.println(child);
                    System.out.println("========================");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    @Test
    public void create() throws KeeperException, InterruptedException {
        String node_create = client.create("/wt", "beauty".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = client.getChildren("/", true);

        for (String child : children)
            System.out.println(child);


        Thread.sleep(Long.MAX_VALUE);
    }


    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat exists = client.exists("/wt3", false);
        System.out.println(exists==null?"not exist":"exist");
    }
}
