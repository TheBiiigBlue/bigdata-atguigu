package com.bigblue.jvm;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/6
 */
public class CodeBlock {

    /**
     * 静态代码块 > 成员代码块 > 构造
     * 静态优先，且只执行一次
     * 其次成员代码块，每new一次，执行一次
     * 最后是构造
     */

    {
        System.out.println("CodeBlock 成员代码块");
    }

    static {
        System.out.println("CodeBlock 静态代码块");
    }

    public CodeBlock() {
        System.out.println("CodeBlock 内部构造");
    }

    public static void main(String[] args) {
        System.out.println("CodeBlock Main");
        new CodeTest();
        System.out.println("------------------");
        new CodeTest();
        System.out.println("------------------");
        new CodeBlock();
    }

}

class CodeTest {

    public CodeTest() {
        System.out.println("CodeTest 内部构造");
    }

    {
        System.out.println("CodeTest 成员代码块");
    }

    static {
        System.out.println("CodeTest 静态代码块");
    }

}
