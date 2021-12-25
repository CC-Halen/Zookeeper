package com.cdw.case2;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: cdw
 * @date: 2021/12/25 10:30
 * @description:
 */
public class DistributedLock {
    private final String connectString = "47.108.192.114:2182";
    private final int sessionTime = 50000;
    private final ZooKeeper zk;
    private CountDownLatch connectLatch = new CountDownLatch(1);
    private CountDownLatch waitLatch = new CountDownLatch(1);
    private String waitPath;
    private String currentNode;

    public DistributedLock() throws IOException, InterruptedException, KeeperException {
        //获取连接
        zk = new ZooKeeper(connectString, sessionTime, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //连接上后释放connectLatch
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
                    connectLatch.countDown();

                //waitLatch需要释放
                if (watchedEvent.getType()==Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)){
                    waitLatch.countDown();
                }

            }
        });

        //等待连接成功之后才继续操作
        connectLatch.await();


        //判断根节点/locks是否存在
        Stat stat = zk.exists("/locks", false);

        if (stat == null) {
            //不存在就创建根节点
            zk.create("/locks", "locks".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

    }


    //对zk加锁
    public void zkLock() {
        //创建对应的带序列的临时节点
        try {
            currentNode = zk.create("/locks/" + "seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zk.getChildren("/locks", false);
            //判断是否是最小的序列号节点，若是则获取锁，不是则监听上一个节点
            if (children.size() == 1) {
                return;
            } else {
                Collections.sort(children);

                //获取节点名称
                String thisNode = currentNode.substring("/locks/".length());

                int index = children.indexOf(thisNode);

                if (index == -1) {
                    System.out.println("数据异常");
                } else if (index == 0) {
                    return;
                } else {
                    waitPath = "/locks/" + children.get(index - 1);
                    zk.getData(waitPath, true, new Stat());


                    waitLatch.await();
                }
            }


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


    //对zk解锁
    public void unZkLock() {
        //删除节点
        try {
            zk.delete(currentNode,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
