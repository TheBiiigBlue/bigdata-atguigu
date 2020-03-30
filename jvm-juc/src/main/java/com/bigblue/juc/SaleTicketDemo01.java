package com.bigblue.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/3/30
 */
public class SaleTicketDemo01 {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        threadRun(ticket, "A");
        threadRun(ticket, "B");
        threadRun(ticket, "C");
    }

    private static void threadRun(Ticket ticket, String threadName) {
        /**
         * new Thread 并不是直接启动线程运行，多线程是靠底层调度和cpu来控制的
         * Thread.State 线程的状态：NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
         */
        new Thread(() -> { for(int i=0;i<40;i++) ticket.sale(); }, threadName).start();
    }
}

//资源类
class Ticket {
    private int number = 30;
    private Lock lock = new ReentrantLock();

    public void sale() {
        lock.lock();
        try{
            if(number > 0){
                System.out.println(Thread.currentThread().getName() + "\t卖出了第：" + number-- + "\t 还剩：" + number);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}