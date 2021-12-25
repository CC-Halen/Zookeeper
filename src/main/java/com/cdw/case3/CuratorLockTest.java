package com.cdw.case3;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author: cdw
 * @date: 2021/12/25 15:36
 * @description:
 */
public class CuratorLockTest {
    public static void main(String[] args) {
        //创建分布式锁1
        final InterProcessMutex lock1 = new InterProcessMutex(getCuratorFramework(), "/locks");


        //创建分布式锁2
        final InterProcessMutex lock2 = new InterProcessMutex(getCuratorFramework(), "/locks");


        new Thread(new Runnable() {
            public void run() {
                try {
                    lock1.acquire();
                    System.out.println("线程1 获取锁");

                    lock1.acquire();
                    System.out.println("线程1 再获取锁");

                    Thread.sleep(5000);

                    lock1.release();
                    System.out.println("线程1 释放锁");
                    lock1.release();
                    System.out.println("线程1 再次释放锁");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    lock2.acquire();
                    System.out.println("线程2 获取锁");

                    lock2.acquire();
                    System.out.println("线程2 再获取锁");

                    Thread.sleep(5000);

                    lock2.release();
                    System.out.println("线程2 释放锁");
                    lock2.release();
                    System.out.println("线程2 再次释放锁");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static CuratorFramework getCuratorFramework() {

        ExponentialBackoffRetry policy = new ExponentialBackoffRetry(3000, 3);

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("47.108.192.114:2182")
                .connectionTimeoutMs(50000)
                .sessionTimeoutMs(50000)
                .retryPolicy(policy).build();

        client.start();
        System.out.println("zookeeper 启动成功！");

        return client;
    }
}
