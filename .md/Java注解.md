# 1、@Target

**@Target**表示我们的注解可以用在哪些地方

```java
ElementType.TYPE：能修饰类、接口或枚举类型
ElementType.FIELD：能修饰成员变量
ElementType.METHOD：能修饰方法
ElementType.PARAMETER：能修饰参数
ElementType.CONSTRUCTOR：能修饰构造器
ElementType.LOCAL_VARIABLE：能修饰局部变量
ElementType.ANNOTATION_TYPE：能修饰注解
ElementType.PACKAGE：能修饰包
```

# 2、@Retention

**@Retention**表示我们的注解在什么地方还有效

1. RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留，编译时就会被忽略。
2. RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留，在class文件中存在,但JVM将会忽略
3. RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留，所以他们能在运行时被JVM或其他使用

# 3、@Documented

**@Documented**注解表明这个注释是由 javadoc记录的，在默认情况下也有类似的记录工具。 如果一个类型声明被注释了文档化，它的注释成为公共API的一部分。

# 4、@Inherited

1. **类继承关系中@Inherited的作用：**类继承关系中，子类会继承父类使用的注解中被@Inherited修饰的注解。
2. **接口继承关系中@Inherited的作用：**接口继承关系中，子接口不会继承父接口中的任何注解，不管父接口中使用的注解有没有被@Inherited修饰。
3. **类实现接口关系中@Inherited的作用：**类实现接口时不会继承任何接口中定义的注解。


当以后我们在定义一个作用于类的注解时候，如果希望该注解也作用于其子类，那么可以用@Inherited 来进行修饰。

详细可以看此处

https://blog.csdn.net/sunnyzyq/article/details/119736442?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522167400982616800211530354%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=167400982616800211530354&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-2-119736442-null-null.142^v71^wechat,201^v4^add_ask&utm_term=%40inherited%E6%B3%A8%E8%A7%A3%E7%9A%84%E4%BD%9C%E7%94%A8&spm=1018.2226.3001.4187

