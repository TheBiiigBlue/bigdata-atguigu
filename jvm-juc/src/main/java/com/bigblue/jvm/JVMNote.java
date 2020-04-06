package com.bigblue.jvm;

import java.util.Random;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/5
 */
public class JVMNote {

    /**
     * 类加载器：
     *  1.BootStrap类加载器，最高层，rt.jar包(Runtime)下的内容
     *  2.Extention类加载器，中间层，ext包下的内容
     *  3.Application加载器，最底层，用户应用加载器
     *
     *  双亲委派机制：
     *      jvm加载一个class时，首先通过Bootstrap加载，如果没找到，则去下层查找
     *      接着在Extention加载，如果没找到，则去下层查找
     *      再接着再Application加载，如果没找到，则报ClassNotFoundException
     *
     *  沙箱安全机制：
     *     为了保证底层原代码安全，不被覆盖
     *
     *  Native方法：
     *      一个关键字，有声明，无实现，java调用底层c++代码
     *      native方法进入本地方法栈
     *
     *  PC寄存器：
     *      每个线程都有一个pc寄存器，记录了方法之间调用和执行情况。
     *      用来存储指向下一条指令的地址，也即将执行的指令代码
     *      它是当前线程所执行的字节码的行号指示器
     *
     *  方法区：(数据供线程共享，存在垃圾回收，但几乎没有)
     *      1.存储了每一个类的结构信息
     *      2.方法区是规范，在不同虚拟机里实现是不一样的，最典型的就是永久代(PermGen)和元空间(MetaSpace)
     *          (永久代和元空间是方法区的具体实现)，而下面堆的逻辑部分中有永久代，表示方法区是堆的一个逻辑部分，
     *          但方法区还有一个别名叫Non-Heap非堆，就是为了和堆分开
     *      3.但是成员变量是分配在堆区的
     *
     *  Stack栈(每个线程单独分配一个栈，不存在垃圾回收)
     *      1.栈管运行，堆管存储
     *      2.栈是在线程创建时创建，跟随线程的生命周期，线程结束栈内存释放，不存在垃圾回收，线程私有。
     *      3.栈保存：8种基本类型的变量+对象的引用变量+实例方法
     *      4.java方法被加载到栈内存为栈针，栈针中存储：本地变量(输入输出参数以及方法内的变量)
     *
     *
     *  堆Heap
     *      一个JVM只存在一个堆内存，大小可调节。类加载器读取了类文件后，需要把类、方法、常变量
     *      放到堆内存中，保存所有引用类型的真实信息，堆内存逻辑上分为三部分：新生代+年老代+永久代
     *      1.新生代 1/3 堆空间
     *          1) 伊甸园 Eden Space   8/10 新生代空间
     *          2) 幸存0区 Survivor 0 Space 也叫from区  1/10 新生代空间
     *          3) 幸存1区 Survivor 1 Space 也叫to区  1/10 新生代空间
     *      2.年老代 Tenure Generation Space   2/3 堆空间
     *      3.永久代 Permanent Space，JAVA8之后叫元空间 Meta Space(就是上面说的方法区，具体实现)
     *
     *      新new出来的对象在Eden区，当Eden区中数量达到阈值，则进行一次Young GC对Eden区清理，留下来的对象
     *      转入幸存0区，0区达到阈值，进入1区，1区又满了，转入年老代
     *      年老代也满了，则会进行一次full GC，fullGC多次发现年老代没办法清理出来内存，报错OOM，堆内存溢出。
     *
     *      新生代占 1/3 堆空间，年老代占 2/3 堆空间，新生代中，Eden占 8/10， from和to各占 1/10 新生代空间
     *
     *      MinorGC(YGC)过程：(复制->清空->互换)
     *      1. eden、survivorFrom复制到survivorTo，年龄+1
     *          首先，当Eden区满的时候触发第一次GC，把还活着的对象拷贝到SurvivorFrom区，当Eden区再次触发GC时，
     *          扫描Eden和SruvivorFrom两个区，还活着的，会直接复制到survivorTo区，并且年龄+1，如果这时有对象年龄
     *          到了老年的标准，则复制到老年代中。
     *      2. 清空eden、survivorFrom
     *          然后清空Eden和survivorFrom中的对象，只保留survivorTo中对象，再把survivorTo互换为survivorFrom，
     *          参与下次GC，即复制后互换，谁空谁是To
     *      3. SurvivorTo和SurvivorFrom互换
     *          最后，survivorTo和survivorFrom互换，原来survivorTo成为下次GC时的survivorFrom区，部分对象会在From
     *          和To区中复制来复制去，如此交换15次(由JVM参数：MaxTenuringThreshold决定，默认15)，最终还存活，则
     *          存入老年代。
     *
     *      永久代(jdk8叫元空间)，也就是方法区，一个具体实现
     *          是一个常驻内存区域，用于存放JDK自身所携带的Class，Interface的元数据，即它存储的是运行环境必须的类信息
     *          被装载进此区域的数据是不会垃圾回收期回收掉的，关闭JVM才会释放此区域所占用的内存。
     *          永久代和元空间的最大区别在于，永久代使用JVM堆内存，元空间使用的是本机物理内存而不在虚拟机中
     *          因此元空间大小仅受本地内存限制。元数据放入native memory，字符串池和类的静态变量放入java堆中，这样可以加载
     *          多少类的元数据就不再由MaxPermSize控制，而由系统的实际可用空间来控制
     *
     *  堆参数调优：
     *      -Xms: 设置初始分配大小，默认为物理内存的 1/64
     *      -Xmx: 最大分配内存，默认为物理内存的 1/4
     *      -XX:+PrintGCDetails : 输出详细的GC处理日志
     *          生产中建议把Xms和Xmx调一样大，避免JVM内存忽高忽低，出现一些莫名其妙的问题
     *          配置vm：-Xms1024m -Xmx1024m -XX:+PrintGCDetails
     *
     *  GC日志：
     *      YOUNG GC日志：
     *          [GC (Allocation Failure)    分配出错，内存不足
     *          [PSYoungGen: 2011K->512K(2560K)] 2011K->756K(9728K), 0.0009962 secs]
     *          [新生代之前占用 -> GC后新生代占用(新生代总大小)] 堆之前占用->GC后堆内存占用(堆总大小)， GC耗时
     *          [Times: user=0.00 sys=0.00, real=0.00 secs] 用户耗时，系统耗时，gc耗时
     *      FULL GC 日志：
     *          [Full GC (Allocation Failure)   分配出错，内存不足
     *          [PSYoungGen: 0K->0K(2560K)]     [新生代之前占用 -> GC后新生代占用(新生代总大小)]
     *          [ParOldGen: 693K->677K(7168K)] 693K->677K(9728K),
     *          [年老代之前占用 -> GC后年老代占用(年老代总大小)] 堆之前占用->GC后堆内存占用(堆总大小)
     *          [Metaspace: 3083K->3083K(1056768K)], 0.0046297 secs]    [元空间之前占用 -> GC后元空间占用(元空间总大小)] gc耗时
     *          [Times: user=0.00 sys=0.00, real=0.01 secs] 用户耗时，系统耗时，gc耗时
     *
     *  什么是GC？
     *      次数上频繁收集Young区，次数上较少收集Old区，基本不动元空间
     *      JVM在进行GC时，并非在三个内存区域一起回收，大部分时候回收的都是新生代。
     *      Minor GC和 Major GC区别
     *          MinorGC：只针对新生代区域的GC，因为大多数java对象存活率不高，所以MinorGC非常频繁，回收速度也比较快
     *          MajorGC： 指发生在年老代的GC，MajorGC经常会伴随至少一次的MinorGC(但不是绝对的)，一般速度要比MinorGC慢10倍以上
     *
     *  4大垃圾回收算法：
     *      1.引用计数法(JVM一般不采用这种方式): 该对象被引用则计数+1,断开引用则计数-1,为0说明为垃圾对象需要回收
     *          缺点：每次对对象赋值时均要维护引用计数器，且计数器本身也有一定的消耗；较难处理循环引用的问题。
     *      2.复制算法(Copy)：新生代使用的是MinorGC，这种GC算法采用的就是复制算法
     *          MinorGC时使用复制算法将Eden和From区还存活的对象复制到To区，并把年龄+1，当年龄达到阈值(默认15，
     *          通过-XX:MaxTenuringThreshold来设定)，这些对象就会成为老年代。
     *          因为新生代对象基本都是朝生夕死(90%以上)，所以新生代采用复制算法，其基本思想就是将内存分为两块，每次
     *          只用其中一块，当这一块内存用完，就将还活着的对象复制到另外一块上面，所以不会产生内存碎片。
     *          缺点：占空间，to跟from占用一样的空间大小，并且不要保证对象死亡率高一些，这样复制的少，也会很快
     *      3.标记清除(Mark-Sweep): 年老代一般采用标记清除与标记压缩的混合实现
     *          当内存耗尽时，GC线程会触发并将程序暂停，随后将要回收的对象标记一遍，再统一回收，完成标记清理后再让程序恢复运行。
     *          优点：不需要额外空间。
     *          缺点：两次扫描耗时严重且需要停止应用程序，会产生内存碎片(内存空间不连续，JVM不得不维持一个内存的空闲列表，又是一种开销，
     *          而且在分配数组对象时，连续空间不好找)
     *      4.标记压缩(Mark-Compact): 也叫标记整理，全称标记清除压缩
     *          前面步骤和标记清除算法一样，然后再次扫描并往一端滑动存活对象。
     *          优点：没有内存碎片，可以利用bump-the-pointer，整理干净，慢工出细活
     *          缺点：多次扫描且需要移动对象成本，效率不高
     *          年老代因为区域较大，且对象存活率高，所以使用标记清除和标记压缩的混合实现，即多次GC后整理内存
     *
     *      整理：
     *          内存效率： 复制算法 > 标记清除算法 > 标记整理算法
     *          内存整齐度：复制算法=标记整理算法 > 标记清除算法
     *          内存利用率: 标记清除=标记整理 > 复制算法
     *          没有最好，只有更适合哪个代，所以叫分代回收算法，而在JDK9增加了一个效率高且没有碎片的G1算法
     * @param args
     */
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        int processors = runtime.availableProcessors();
        System.out.println("processors = " + processors);
        System.out.println("-Xms: TOTAL_MEMORY = " + totalMemory + "(字节)、" + (totalMemory/(double)1024/1024) + "MB");
        System.out.println("-Xmx: MAX_MEMORY = " + maxMemory + "(字节)、" + (maxMemory/(double)1024/1024) + "MB");

//        String str = "www.atguigu.com";
//        while (true) {
//            str += str + new Random().nextInt(88888888) + new Random().nextInt(99999999);
//        }
        //直接创建一个15M大对象，然后直接OOM
        byte[] bytes = new byte[15 * 1024 * 1024];
    }

}
