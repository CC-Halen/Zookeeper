package com.cdw.case1;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author: cdw
 * @date: 2021/12/24 21:18
 * @description:
 */
public class DistributeServer {
    private String address = "47.108.192.114:2182";
    private int session = 50000;
    private ZooKeeper zk;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeServer server = new DistributeServer();
        //获取zookeeper连接
        server.getConnect();

        //注册服务器到zookeeper集群
        for (int i = 0; i < args.length; i++) {
            server.register(args[i]);
        }

        //启动业务逻辑
        server.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void register(String hostName) throws KeeperException, InterruptedException {
        String node = zk.create("/servers/" + hostName, hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(hostName + " is online.");
    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(address, session, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }
}
