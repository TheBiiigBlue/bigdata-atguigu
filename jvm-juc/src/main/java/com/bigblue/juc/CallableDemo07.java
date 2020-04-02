package com.bigblue.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/2
 */
public class CallableDemo07 {
    /**
     * callable接口相比于runnable接口主要区别：
     *  1.callable有返回值，通过指定泛型
     *  2.callable是@FunctionalInterface,可以使用lambda表达式
     *  3.callable的call方法有异常
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask(new CallableTest());
        new Thread(futureTask, "A").start();
        Integer result = futureTask.get();
        System.out.println(result);
    }
}

class CallableTest implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("**********invoke call method");
        return 1024;
    }
}