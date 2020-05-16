package com.bigblue.jvm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/21
 */
public class VolatileTest {

    /**
     * volatile只能保证可见性，不能保证原子性
     * inc++是非原子操作，这一步其实是三步：取inc变量的值、加1、赋值
     *
     * 假如某个时刻变量inc的值为10，
     * 　　线程1对变量进行自增操作，线程1先读取了变量inc的原始值，然后线程1被阻塞了；
     * 　　然后线程2对变量进行自增操作，线程2也去读取变量inc的原始值，由于线程1只是对变量inc进行读取操作，而没有对变量进行修改操作，所以不会导致线程2的工作内存中缓存变量inc的缓存行无效，所以线程2会直接去主存读取inc的值，发现inc的值时10，然后进行加1操作，并把11写入工作内存，最后写入主存。
     * 　　然后线程1接着进行加1操作，由于已经读取了inc的值，注意此时在线程1的工作内存中inc的值仍然为10，所以线程1对inc进行加1操作后inc的值为11，然后将11写入工作内存，最后写入主存
     *
     *  线程1对变量进行读取操作之后，被阻塞了的话，并没有对inc值进行修改。然后虽然volatile能保证线程2对变量inc的值读取是从内存中读取的，但是线程1没有进行修改，所以线程2根本就不会看到修改的值。
     *  线程2修改后会通知其他线程缓存失效，但是此时线程1已经进行了读取操作了，所以线程1拿着老的值+1覆盖。
     *
     *  解决方式： 1.加sychronized，2.用lock，3.用AtomticInteger
     */

    public volatile int inc = 0;
//    public volatile AtomicInteger inc =new AtomicInteger();

    public void increase() {
        inc++;
//        inc.getAndIncrement();
    }

//    public synchronized void increase() {
//        inc++;
//    }

//    private Lock lock = new ReentrantLock();
//    public void increase() {
//        lock.lock();
//        try {
//            inc++;
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            lock.unlock();
//        }
//
//    }

    public static void main(String[] args) {
        final VolatileTest test = new VolatileTest();
        for(int i=0;i<10;i++){
            new Thread(() -> {
                for (int j = 0; j < 1000; j++)
                    test.increase();
            }).start();
        }

        while(Thread.activeCount()>1)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
    }
}
