package com.bigblue.jvm;

import java.util.concurrent.TimeUnit;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/6
 */
public class JMMTest {

    /**
     * JMM  即Java Memory Model  java内存模型，特征：可见性，原子性，有序性
     *  解释： 由于JVM运行程序的实体是线程，而每个线程创建时JVM都会为其创建一个工作内存(栈空间)，
     *  工作内存是每个线程私有的数据区域，而Java内存模型中规定所有变量都存储在主内存中，主内存是
     *  共享内存区域，所有线程都可以访问，但线程对变量的操作必须在自己的工作内存中进行，首先要将
     *  变量从主内存拷贝到线程自己的工作内存空间，然后对变量操作，完成后再讲变量写回主内存，线程
     *  间的通信(传值)必须通过主内存来完成。
     *
     *  可见性：即通知机制，内存中的值变了，通知其他线程
     *  java中使用volatile修饰的变量就具备这种可见性
     *
     * 1. volatile， 是java虚拟机提供的轻量级同步机制，volatile修饰的变量，当该变量的值被修改，
     *      则会通知所有线程该值已被修改，请重新同步该值
     */

    public static void main(String[] args) {
        Person person = new Person();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            person.change();
            System.out.println(Thread.currentThread().getName() + "\t update age, age = " + person.age);
        }, "AAA").start();

        //当age被修改后，跳出循环，结束程序
        //当age不被volatile修饰时，即使上面的线程修改了age，但main线程并不知，出现无线循环
        //当age被volatile修饰，上面修改了age，通知main，再次判断age != 10，退出循环
        while(person.age == 10) {

        }

        System.out.println(Thread.currentThread().getName() + "\t thread is finish");
    }
}

class Person {
//    int age = 10;
    volatile int age = 10;
    public void change() {
        this.age = 20;
    }
}
