### 1、八大基本数据类型？
int short long float double byte char boolen
### 2、==和equals区别？
1、 ==比较基本数据类型时比较的是数据表面内容而比较对象时比较的是对象地址 

2、 equals是对对象内容的比较
### 3、String,StringBuilder,StringBuffer区别？
1、 String使用字符数组实现，是不可变的，每次对String进行操作都会生成一 个新的变量复制过去,每次修改会频繁产生临时变量消耗内存，线程安全 

2、 StringBuffer也是用字符数组，但是没有被final修饰，修改时直接在原数组中操作，线程安全  

3、 StringBuilder是可变的提供了append等方法修改字串内容，线程不安全

* 如果需要频繁修改字符串并且在多线程环境下使用，应该使用StringBuffer。     
* 如果只在单线程环境下使用，并且不需要频繁修改字符串，可以使用String。     
* 如果只在单线程环境下使用，并且需要频繁修改字符串，可以使用StringBuilder。
### 4、HashMap的jdk1.8之前和之后的结构区别？ HashMap的put流程？
1.8以前是数组+链表进行存储，链表过长时查询缓慢，1.8以后引入红黑树，长度超过一定限制链表转化为红黑树

1、判断键值对数组table[i]是否为空（null）或者length=0，是的话就执行resize()方法进行扩容。

2、不是就根据键值key计算hash值得到插入的数组索引i。

3、判断table[i]==null，如果是true，直接新建节点进行添加，如果是false，判断table[i]的首个元素是否和key一样，一样就直接覆盖。

4、判断table[i]是否为treenode，即判断是否是红黑树，如果是红黑树，直接在树中插入键值对。

5、如果不是treenode，开始遍历链表，判断链表长度是否大于8，如果大于8就转成红黑树，在树中执行插入操作，如果不是大于8，就在链表中执行插入；在遍历过程中判断key是否存在，存在就直接覆盖对应的value值。

6、插入成功后，就需要判断实际存在的键值对数量size是否超过了最大容量threshold，如果超过了，执行resize方法进行扩容。
### 5、介绍一下加锁方式有哪些？
1、synchronized
对象锁synchronized（object）{….},方法或语句块执行完手动释放  
2、Lock 
可以锁定任意一段代码，但是需要手动释放  
3、ReadWriteLock 读写锁
保证读写互斥 写写互斥 读读不互斥
### 6、线程池的工作原理
* 核心原理：线程复用、资源控制
* 线程池大小:  
  **对于CPU密集型应用**：线程数应接近CPU核心数，以避免过多的线程上下文切换开销。
  N_threads = N_cpu + 1 (一个额外的线程用于在发生页错误等暂停时，确保CPU时钟周期不会被浪费)  
  **对于IO密集型应用**：线程数可以设置得更多一些，因为CPU有很多空闲时间可以去执行其他线程的任务。
  N_threads = N_cpu * U_cpu * (1 + W/C)
  N_cpu：CPU核心数（可通过 Runtime.getRuntime().availableProcessors() 获取）
  U_cpu：期望的CPU利用率（0 <= U_cpu <= 1）
  W/C：等待时间（Wait）与计算时间（Compute）的比值
### 7、使用线程池的好处
1、避免重复的线程创建与销毁
2、维护适当数量的线程避免同时处理过多线程
3、统一线程管理
### 8、安卓中Activity的生命周期有哪些
onCreate onStart onResume onPause onStop onDestory
onRestart onSaveInstanceState onRestoreInstanceState
onNewIntent(处于栈顶时调用startActivity)
### 9、安卓中Activity的启动方式有哪些？
有4种Standard SingleTop SingleTask SingleInstance
### 10、线程安全的单例
```js/java/c#/text
  public class Singleton {
    private static class SingletonHolder {
       private static final Singleton INSTANCE = new Singleton();
    }
    private Singleton() {}
    public static Singleton getInstance() {
       return SingletonHolder.INSTANCE;
    }
  }
```
### 11、jvm jre jdk分别是什么之间的关系
JDK：java开发工具包包括一些类库以及命令行工具、编译工具运行工具、文档生成工具
JRE：java运行环境包含了包含了JVM 虚拟机以及核心类库
JVM：java虚拟机，java跨平台核心部分  

***三者关系：***
* JDK 包含了 JRE，因此安装了JDK后，就不需要再单独安装JRE。

* JRE 包含了 JVM，提供了运行Java字节码的环境。

* JVM 是JRE的一部分，负责将字节码转换成机器指令。

### 12、Fragment与 Activity
[Fragment与 Activity详解](https://www.zhihu.com/tardis/zm/art/426927707?source_id=1005)

### 13、设计模式
* 创建型模式：
    单例模式、工厂模式、建造者模式
* 结构型模式：
    适配器模式、代理模式、装饰器模式
* 行为型模式：
    观察者模式、责任链模式

### 14、自定义view
测量、绘制  
Android 的绘制核心是 Canvas + Paint。

### 15、TCP、UDP是哪一层，传输层和网络层界限在哪
传输层，网络层是主机到主机之间 传输层是进程到进程之间 使用进程号区分  
TCP 可靠的 面向连接的 (三次握手,四次挥手;重传机制;流量控制;拥塞控制)
UDP 不可靠的 无连接的 低延迟
三次握手 四次挥手
```
fun main() = runBlocking {
    // 创建容量为5的缓冲通道
    val channel = Channel<Int>(capacity = 5)
    
    // 生产者协程
    val producer = launch {
        repeat(10) { i ->
            delay(100) // 模拟生产耗时
            channel.send(i)
            println("生产: $i (缓冲区剩余: ${channel.capacity - channel.size})")
        }
        channel.close() // 生产完成后关闭通道
    }

    // 消费者协程
    val consumer = launch {
        channel.consumeEach { item ->
            delay(200) // 模拟消费耗时
            println("消费: $item")
        }
    }

    // 等待生产和消费完成
    joinAll(producer, consumer)
}
```

### 16、进程间通信方法（IPC）
广播、AIDL、ContentProvider、Binder

### 17、死锁的必要条件
不可剥夺，资源互斥，占有且等待，循环等待

### 18、volatile作用
保证内存可见性以及禁止指令重排

## 19、进程与线程的关系
一个进程可能包含多个线程, 进程是资源分配的基本单位。
线程是CPU调度的最小单元 进程间切换资源消耗更多,线程之间共享内存，
线程切换更方便而进程间通信需要IPC。



