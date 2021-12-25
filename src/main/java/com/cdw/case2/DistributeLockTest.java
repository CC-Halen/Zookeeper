package com.cdw.case2;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * @author: cdw
 * @date: 2021/12/25 15:05
 * @description:
 */
public class DistributeLockTest {
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        final DistributedLock lock1 = new DistributedLock();
        final DistributedLock lock2 = new DistributedLock();

        new Thread(new Runnable() {
            public void run() {
                try {
                    lock1.zkLock();
                    System.out.println("线程1启动，获取到锁");
                    Thread.sleep(5000);

                    lock1.unZkLock();
                    System.out.println("线程1释放锁");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        new Thread(new Runnable() {
            public void run() {
                try {
                    lock2.zkLock();
                    System.out.println("线程2启动，获取到锁");
                    Thread.sleep(5000);

                    lock2.unZkLock();
                    System.out.println("线程2释放锁");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
