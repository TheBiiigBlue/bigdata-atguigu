package com.bigblue.jvm;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/3
 */
public class MyObject {

    public static void main(String[] args) {
        //系统自带的，使用顶层的BootStrap装载器
        Object obj = new Object();
        System.out.println(obj.getClass().getClassLoader());

        //使用自己的
        MyObject myObject = new MyObject();
        System.out.println(myObject.getClass().getClassLoader().getParent().getParent());
        System.out.println(myObject.getClass().getClassLoader().getParent());
        System.out.println(myObject.getClass().getClassLoader());
    }
}
