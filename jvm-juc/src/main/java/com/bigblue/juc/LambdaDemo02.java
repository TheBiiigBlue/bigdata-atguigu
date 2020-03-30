package com.bigblue.juc;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/3/30
 */
public class LambdaDemo02 {

    public static void main(String[] args) {
//        Foo foo = () -> { System.out.println("Hello"); };
//        foo.sayHello();

        Foo foo = (i, j) -> {
            System.out.println("invoke add method");
            return i + j;
        };
        System.out.println(foo.add(3, 5));
        System.out.println(foo.mul(3, 5));
        System.out.println(Foo.div(5, 3));
    }
}

/**
 * 标识为函数式类，如果接口中只定义一个方法，则jvm编译时默认会加上@FunctionalInterface注解
 * 只有函数式方法才能用lambda表达式
 * 如果接口中定义了多个方法，则默认当做普通接口
 * jdk8之后，接口可以有默认实现，使用default标识，并给出实现，default的方法可以有多个
 * jdk8之后，接口可以给静态方法
 */
@FunctionalInterface
interface Foo {

//    void sayHello();

    int add(int i, int j);

    default int mul(int i, int j) {
        return i * j;
    }

    static int div(int i, int j) {
        return i / j;
    }
}
