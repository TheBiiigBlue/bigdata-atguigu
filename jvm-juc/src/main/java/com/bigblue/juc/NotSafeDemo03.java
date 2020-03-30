package com.bigblue.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/3/30
 */
public class NotSafeDemo03 {

    /**
     * 1.故障现象： java.util.ConcurrentModificationException
     * 2.导致原因： 高并发多线程争抢同一资源类，且没加锁
     * 3.解决方法：
     *  3.1 使用vector，线程安全，new Vector<>();
     *  3.2 使用集合工具类，将非安全转为安全，Collections.synchronizedList(new ArrayList<>());
     *  3.3 使用juc包下的写时复制技术，new CopyOnWriteArrayList<>();
     * 4.优化建议： 使用juc包下的写时复制技术
     *
     */

    /**
     * 写时复制技术：
     *  CopyOnWrite容器即写时复制容器，往一个容器添加元素的时候，不直接往当前容器Object[]添加，
     *  而是先将当前容器Object[] 进行copy，复制出一个新的newElements，然后在新的容器中添加，
     *  添加元素之后，再将原容器的引用指向新的容器setArray(newElements)，这样的好处是可以对
     *  copyOnWrite容器进行并发的读而不需要加锁，因为当前容器不会添加任何元素，
     *  所以copyOnWrite容器也是一种读写分离的思想，读和写不同的容器
     * @param args
     */

    public static void main(String[] args) {
        listNotSafe();

        Set<String> set = new HashSet<>();
    }

    private static void listNotSafe() {
        /**
         * arrayList默认底层创建空object数组，当第一次add元素时
         * 才扩容为默认的10个长度，可以理解为懒加载
         * arrayList扩容为扩原来的一半，取整
         * hashmap扩容为原来的一倍，也就是2^n
         */
//        List<Object> list = new ArrayList<>();
//        List<Object> list = new Vector<>();
//        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        List<Object> list = new CopyOnWriteArrayList<>();
//        list.add(123);
//        System.out.println(list);

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().subSequence(0, 7));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}
