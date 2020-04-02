package com.bigblue.juc;

import java.util.concurrent.TimeUnit;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/3/31
 */
public class Lock8Demo05 {

    /**
     * 1.标准访问，在两个线程之间加sleep休眠，先打印哪个：    先打印email
     * 2.在email方法中休眠4秒，先打印哪个(两个方法都有同步锁)： 先打印email，因为phone对象锁被email获取
     * 3.新增的普通方法sayHello(无同步锁)，先打印哪个：   先打印sayHello，因为sayHello不用争夺phone对象锁
     * 4.新建一个phone，调用sendSms，先打印哪个：     先打印sendSms，因为phone对象锁不是同一个
     * 5.两个静态同步方法，同一部手机，先打印哪个： 先打印email，因为静态同步，是同一个类锁
     * 6.两个静态同步方法，两部手机，先打印哪个： 先打印email，同一个类锁
     * 7.一个静态同步方法，一个普通同步方法，同一部手机，先打印哪个： 先打印sms，一个类锁，一个对象锁
     * 8.一个静态同步方法，一个普通同步方法，两部手机，先打印哪个： 先打印sms，一个类锁，一个对象锁
     */

    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        //对应第一点
        Thread.sleep(100);

        new Thread(() -> {
            try {
//                phone.sendSms();
                //对应第三点
//                phone.sayHello();
                //对应第四点
//                phone2.sendSms();
                //对应第五点
//                phone.sendSms();
                //对应第六点
//                phone2.sendSms();
                //对应第七点
//                phone.sendSms();
                //对应第八点
                phone2.sendSms();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
    }
}

class Phone {
    //改为静态，对应第五点
    public static synchronized void sendEmail() throws Exception {
        //对应第二点，休眠4秒，concurrent包下的
        TimeUnit.SECONDS.sleep(4);
        System.out.println("*****sendEmail");
    }

    //改为静态，对应第五点
    //改为非静态，对应第七点
    public /*static*/ synchronized void sendSms() throws Exception {
        System.out.println("*****sendSms");
    }

    public void sayHello() throws Exception {
        System.out.println("*****sayHello");
    }
}
