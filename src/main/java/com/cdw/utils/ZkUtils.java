package com.cdw.utils;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author: cdw
 * @date: 2021/12/24 21:36
 * @description:
 */
public class ZkUtils {
    private static String address = "47.108.192.114:2182";
    private static int session = 50000;
    private static ZooKeeper zk;

    public static void getConnect() throws IOException {
        zk = new ZooKeeper(address, session, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }


    public static void register(String hostName) throws KeeperException, InterruptedException {
        String node = zk.create("/servers", hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostName + "is online.");
    }


    public static void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}
