# 一、了解JVM

## 1、什么是JVM？

**定义：**

Java Virtual Machine - java程序的运行环境（java二进制字节码 的运行环境）

**优点：**

- 一次编写，到处运行
- 自动内存管理，垃圾回收功能
- 数组下表越界检查
- 多态

**比较：**

- jvm：与操作系统进行交互，
- jre：JVM+基础类库
- jdk：JVM+基础类库+编译工具

![image-20230105103700523](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105103700523.png)

## 2、学习JVM有什么用

- 面试
- 理解底层的实现原理
- 中高级程序员的必备技能

## 3、常见的JVM

![image-20230105104737945](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105104737945.png)

## 4、学习路线

![image-20230105104811492](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105104811492.png)

# 二、内存结构

1. 程序计数器
2. 虚拟机栈
3. 本地方法栈
4. 堆
5. 方法区
6. 运行时常量池
7. 直接内存

## 1、程序计数器

![image-20230105105024180](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105105024180.png)



### 1.1、定义

Program Counter Register 程序计数器（寄存器）

- 作用：记住下一条JVM指令的执行地址
- 特点：
  - 是线程私有的
  - 不会存在内存溢出

```
0: getstatic #20 				// PrintStream out = System.out;
3: astore_1 					// --
4: aload_1 						// out.println(1);
5: iconst_1 					// --
6: invokevirtual #26 			// --
9: aload_1 						// out.println(2);
10: iconst_2 					// --
11: invokevirtual #26 			// --
14: aload_1 					// out.println(3);
15: iconst_3 					// --
16: invokevirtual #26 			// --
19: aload_1 					// out.println(4);
20: iconst_4 					// --
21: invokevirtual #26 			// --
24: aload_1 					// out.println(5);
25: iconst_5 					// --
26: invokevirtual #26 			// --
29: return
```

## 2、虚拟机栈

![image-20230105110813097](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105110813097.png)

### 2.1、定义

Java Virtual Machine Stacks （Java虚拟机栈）

- 每个线程运行时所需要的内存，称为虚拟机栈
- 每个栈由多个栈帧组成，对应着每次方法调用时所占用的内存。
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

**问题辨析：**

1. 垃圾回收是否涉及栈内存？

不涉及，栈内存是方法调用产生的栈帧内存，栈帧内存在每次方法调用后会被弹出栈，自动被回收，不需要垃圾回收管理。

2. 栈内存分配越大越好吗？

不是，栈内存分配的越大，线程就会及减少，因为物理内存的大小固定，栈内存越大，线程越少。

3. 方法内的局部变量是否线程安全？

- 如果方法内部的局部变量没有逃离方法的作用访问，它是线程安全的。
- 如果是局部变量引用了对象，并逃离方法的作用范围，需要考虑线程安全。

**报错信息：java.lang.StackOverflowError**

### 2.2、栈内存溢出

- 栈帧过多导致内存溢出

![image-20230105140558268](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105140558268.png)

- 栈帧过大导致内存溢出

![image-20230105140630587](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105140630587.png)

### 2.3、线程运行诊断

**案例1：** cpu占用过多

定位：

- 进入dos命令窗口：使用 **top** 命令，查看哪个进程对CPU占用过高。
- ps H -eo pid,tid,%cpu | grep 进程id （用ps命令进一步定位是哪个线程引起的cpu占用过高）
- jstack 进程id，可以根据线程id 找到有问题的线程，进一步定位到问题代码的源码行号。

![image-20230105152042611](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105152042611.png)

**案例2：** 程序运行很长时间没有结果

按照上述步骤查询：

![image-20230105153849265](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105153849265.png)

## 3、本地方法栈

java虚拟机调用本地方法时，给本地方法提供的内存空间。

![image-20230105153208039](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105153208039.png)

**本地方法：** java虚拟机不能与操作系统底层交互，需要c/c++语言编写的方法进行交互。如Object类中的clone()，hashCode()，wait();

## 4、堆

虚拟机所管理的内存中最大的一块，Java堆是所有线程共享的一块区域，在虚拟机启动的时候创建。

![image-20230105153233288](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105153233288.png)

### 4.1、定义

**Heap堆：** 通过new关键字创建的对象都会使用堆内存

**特点：**

- 它是线程共享的，堆中的对象都需要考虑线程安全问题。
- 有垃圾回收机制

### 4.2、堆内存溢出

随着对象数量的增加，总容量触及最大堆的容量限制后就会产生堆内存异常。

Java堆内存的 **OutOfMemoryError** 异常是实际应用中最常见的内存溢出异常情况，异常堆栈信息：**java.lang.OutOfMemoryError:Java heap space** 

### 4.3、堆内存诊断

1. **jps工具**：查看当前系统中有哪些 java 进程
2.  **jmap 工具：**查看堆内存占用情况 jmap - heap 进程id
3.  **jconsole 工具：**图形界面的，多功能的监测工具，可以连续监测
4. **jvisualvm：** 可视化虚拟器

## 5、方法区

是各个线程共享的内存区域，它用于存储已被虚拟机加载的类型信息，

![image-20230105161411847](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105161411847.png)

### 5.1、组成

**Java 1.6 内存结构**

![image-20230105163022254](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105163022254.png)

**Java 1.8 内存结构**

![image-20230105163110565](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105163110565.png)

### 5.2、方法区内存溢出

- 1.8 以前会导致永久代内存溢出

```
* 演示永久代内存溢出 java.lang.OutOfMemoryError: PermGen space
* -XX:MaxPermSize=8m
```

- 1.8 之后会导致元空间内存溢

```
* 演示元空间内存溢出 java.lang.OutOfMemoryError: Metaspace
* -XX:MaxMetaspaceSize=8m
```

## 6、运行时常量池

- 常量池，就是一张表，虚拟机指令根据这张常量表找到要执行的类名，方法名，参数类型，字面量等信息。
- 运行时常量池，常量池是*.class文件中的，当该类被加载，它的常量池信息就会放入运行时常量池，并把里面的符号地址变为真实地址。

### 6.1、StringTable

```java
String s1 = "a";
String s2 = "b";
String s3 = "a" + "b";
String s4 = s1 + s2;
String s5 = "ab";
String s6 = s4.intern();
// 问
System.out.println(s3 == s4);
System.out.println(s3 == s5);
System.out.println(s3 == s6);
String x2 = new String("c") + new String("d");
String x1 = "cd";
x2.intern();
// 问，如果调换了【最后两行代码】的位置呢，如果是jdk1.6呢
System.out.println(x1 == x2);
```

### 6.2、**StringTable** 特性

- 常量池中的字符串仅是符号，第一次用到时才变为对象

- 利用串池的机制，来避免重复创建字符串对象

- 字符串变量拼接的原理是 StringBuilder （1.8）

- 字符串常量拼接的原理是编译期优化

- 可以使用 intern 方法，主动将串池中还没有的字符串对象放入串池。
  1. 1.8 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有则放入串池， 会把串池中的对象返回
  2. 1.6 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有会把此对象复制一份，放入串池， 会把串池中的对象返回



## 7、直接内存

### 7.1、定义

Direct Memory

- 常见于NIO操作，用于数据缓冲区。
- 分配回收成本较高，但读写性能高。
- 不受JVM内存回收管理

### 7.2、分配和回收原理

- 使用了Unsafe 对象完成直接内存的分配回收，并且回收需要调用freeMemory方法。
- ByteBuffer的实现类内部，使用Cleaner（虚引用）来监测ByteBuffer对象，一旦ByteBuffer对象被垃圾回收，那么就会由ReferenceHandler线程通过Cleaner的Clean方法调用freeMemory来释放直接内存。

# 三、垃圾回收

1. 如何判断对象可以回收
2. 垃圾回收算法
3. 分代垃圾回收
4. 垃圾回收器
5. 垃圾回收调优

## 1、如何判断对象可以回收

### 1.1 引用计数法

在对象中添加引用计数器，每当有一个地方引用它时，计数器就加一，当引用失效时，计数器值减一，任何时刻，计数器为零的对象就是不可能再被使用的。

**计数器会遇到的问题：循环引用。**

![image-20230105175614009](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230105175614009.png)

java虚拟机并不会因为两个对象互相引用就放弃回收他们，所以java虚拟机并不是通过引用计数法来判断对象是否存活的。

### 1.2、可达性分析算法	

当前主流的商用语言（Java，C#）的内存管理子系统，都是通过可达性分析算法来判定对象是否存活。**通过一系列被称为 “GC Root” 的根对象作为起始节奏点，从这些节点开始向下搜索，搜索过程所走过的路径成为“引用链”，如果某个对象到 “GC Root” 间没有任何引用链相连，则证明此对象不可能再被使用。 **

- Java虚拟机中的垃圾回收采用可达性分析来探索所有存活的对象。
- 扫描堆中的对象，看是否能够沿着**GC Root**为起点的引用链找到该对象，找不到，表示可以回收。







