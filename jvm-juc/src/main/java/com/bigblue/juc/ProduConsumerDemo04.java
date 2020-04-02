package com.bigblue.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/2
 */
public class ProduConsumerDemo04 {

    /**
     * 题目：两个线程，操作初始值为0的一个变量
     * 实现一个线程对该变量+1，另一个线程对该变量-1
     * 交替实现10轮，变量初始值为零
     *
     * 1.高内聚低耦合前提下，线程操作资源类
     * 2. 多线程下，判断、再干活、再通知
     * 3. 防止虚假唤醒，将if改为while(wait会让出对象锁，让其他线程获取到锁继续执行)
     */

    public static void main(String[] args) {
        Aircondition aircondition = new Aircondition();
        new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    aircondition.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    aircondition.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    aircondition.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
        new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    aircondition.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}

class Aircondition{
    private int number = 0;

    /*public synchronized void increment() throws Exception {
        while*//*if*//*(number != 0){
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName() + "\t生产了" + number );

        //通知其他线程消费
        this.notifyAll();
    }

    public synchronized void decrement() throws Exception {
        while*//*if*//*(number == 0){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "\t消费了" + number);

        //通知其他线程生产
        this.notifyAll();
    }*/

    /**
     * wait、notify、notifyAll必须和sychronized一起使用
     * 新版使用condition.await、condition.signal、condition.signalAll代替
     */

    //使用新的锁代替synchronized
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try{
            while(number != 0) {
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "\t生产了" + number);

            condition.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try{
            while(number == 0) {
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "\t消费了" + number);

            condition.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
