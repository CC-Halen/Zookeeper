package com.cdw.case1;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cdw.utils.ZkUtils.*;

/**
 * @author: cdw
 * @date: 2021/12/24 21:33
 * @description:
 */
public class DistributeClient {

    private String address = "47.108.192.114:2182";
    private int session = 50000;
    private ZooKeeper zk;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        DistributeClient client = new DistributeClient();

        //获取zk连接
       client.getConnect();


        //监听servers下面子节点的新增与删除
        client.getServerList();


        //业务逻辑
        client.business();

    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void getServerList() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren("/servers", true);

        ArrayList<String> servers = new ArrayList<String>();

        for (String child : children) {
            byte[] data = zk.getData("/servers/" + child, false, null);
            servers.add(new String(data));
        }


        for (String server : servers) {
            System.out.println(server);
        }
        System.out.println("=================================");

    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(address, session, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    getServerList();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
