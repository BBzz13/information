# Spring
## spring 配置方式

### 1、Bean相关

```xml
<!--bean标签标示配置bean
id属性标示给bean起名字
class属性表示给bean定义类型-->
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl"/>

<!--scope：为bean设置作用范围，可选值为单例singloton：所有对象为同一个对象，非单例prototype：每次新建一个对象-->
<bean id="bookDao" name="dao" class="com.itheima.dao.impl.BookDaoImpl" scope="prototype"/>

<!--创建工厂bean -->
<bean id="userFactory" class="com.itheima.factory.UserDaoFactory"/>
<!--factory-bean：调用工厂bean factory-method：调用工厂bean'中的工厂方法生成user bean-->
<bean id="userDao" factory-method="getUserDao" factory-bean="userFactory"/>

<!--init-method：设置bean初始化生命周期回调函数-->
<!--destroy-method：设置bean销毁生命周期回调函数，仅适用于单例对象-->
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl" init-method="init" destroy-method="destory"/>

<bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
    <property name="bookDao" ref="bookDao"/>
</bean>

<!--注入简单类型和引用类型-->
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
<!--注入简单类型-->
    <!--property标签：设置注入属性-->
    <!--name属性：设置注入的属性名，实际是set方法对应的名称-->
    <!--value属性：设置注入简单类型数据值-->
    <property name="connectionNum" value="100"/>
<!--注入引用类型-->
    <!--property标签：设置注入属性-->
    <!--name属性：设置注入的属性名，实际是set方法对应的名称-->
    <!--ref属性：设置注入引用类型bean的id或name-->
    <property name="bookDao" ref="bookDao"/>
</bean>

<!--lazy-init：懒加载，Spring延迟加载bean,默认为false,为true时，使用到此bean时才会被加载-->
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl" lazy-init="true"/>

<!-- bean相关-->
<bean
    id="bookDao" 										bean的Id
    name="dao.bookDaoImpl.daoImpl" 						  bean别名
    class="com.itheima.dao.impl.BookDaoImpl"			   bean类型,静态工厂类, FactoryBean类
    scope="singleton" 									 控制bean的实例数量
    init-method="init" 									 生命周期初始化方法
    destroy method="destory" 							  生命周期销毁方法
    autowire="byType"  									 自动装配类型
    factory-method="getInstance" 						  bean工厂方法,应用于静态工厂或实例工厂
    factory-bean="com.itheima.factory.BookDaoFactory" 	    实例工厂bean
    lazy-init="true" 								      控制bean延迟加载
/>

<bean id="bookService" class=" com.itheima.service.impl.BookServiceImpl">
    <constructor-arg name="bookDao" ref="bookDao" /> 			构造器注入引用类型
    <constructor-arg name="userDao" ref="userDao" />
    <constructor-arg name="msg" value= "WARN" /> 				构造器注入简单类型
    <constructor-arg type"java.lang.String" index="3" value="WARN" /> 类型匹配与索引匹配
    <property name="bookDao" ref="bookDao" /> 						 setter注入引用类型
    <property name="userDao" ref="userDao" />
    <property name= "msg" value= "WARN"/>  							setter注入简单类型
    <property name=" names">										setter注入集合类型
        <list>													list集合
            <value> itcast</value> 								集合注入简单类型
            <ref bean="dataSource"/>							集合注入引用类型
        </list>
    </property>
</bean>
```

### 2、context相关

```xml
<!--classpath:*.properties ：设置加载当前工程类路径中的所有properties文件-->
<!--system-properties-mode属性：是否加载系统属性-->
<!--classpath*:*.properties  ：设置加载当前工程类路径和当前工程所依赖的所有jar包中的所有properties文件-->
<!--ignore-unresolvable="true"，这样可以在加载第一个property-placeholder时出现解析不了的占位符进行忽略掉 -->
<!--file-encoding="UTF-8"：若有中文，防止乱码，不需要可以不写。-->
<context:property-placeholder 
                              ignore-unresolvable="true" 
                              location="classpath*:*.properties" 
                              system-properties-mode="NEVER"
                              file-encoding="UTF-8"
                              />            

<!--注解开发扫描包-->
<context:component-scan base-package="com.wuyu.ssm"/>

<!--排除注解-->
<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller" />

<!--这是一条向Spring容器中注册:AutowiredAnnotationBeanPostProcessor、CommonAnnotationBeanPostProcessor、PersistenceAnnotationBeanPostProcessor、RequiredAnnotationBeanPostProcessor。
如果想使用@ Resource 、@ PostConstruct、@ PreDestroy等注解就必须声明CommonAnnotationBeanPostProcessor。
如果想使用@PersistenceContext注解，就必须声明PersistenceAnnotationBeanPostProcessor的Bean。
如果想使用@Autowired注解，那么就必须事先在 Spring 容器中声明 AutowiredAnnotationBeanPostProcessor Bean。
如果想使用 @Required的注解，就必须声明RequiredAnnotationBeanPostProcessor的Bean。-->
<context:annotation-config/>

<!-- 导入相关配置文件 -->
<import resource="spring-mybatis.xml"/>
<import resource="classpath*:/redis/spring-redis.xml"/>

```

### 3、事物配置

```xml
<!--======= 事务配置 Begin ================= -->
<!-- 事务管理器（由Spring管理MyBatis的事务） -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!-- 关联数据源 -->
    <property name="dataSource" ref="dataSource"></property>
</bean>
<!-- 注解方式配置事物 -->
<tx:annotation-driven transaction-manager="transactionManager" />
```

## spring 注解方式

```java
//1、声明当前类为Spring配置类
@Configuration
//2、设置bean扫描路径，多个路径书写为字符串数组格式
@ComponentScan({"com.itheima.service","com.itheima.dao"})
//3、@Component定义bean
@Component("bookDao")
//4、@Repository：@Component衍生注解 用于定义dao层
@Repository("bookDao")
//5、@Service：@Component衍生注解 用于定于service层
@Service
//6、@Scope 为bean设置作用范围，可选值为单例singloton：所有对象为同一个对象，非单例prototype：每次新建一个对象
@Scope("singleton")
//7、@PostConstruct设置bean的初始化方法
@PostConstruct
//8、@PreDestroy设置bean的销毁方法
@PreDestroy
//9、@PropertySource加载properties配置文件
@PropertySource({"jdbc.properties"})
//10、@Import:导入配置信息
@Import({JdbcConfig.class})
//11、@Value：注入简单类型（无需提供set方法）
@Value("${name}")
//12、@Autowired：注入引用类型，自动装配模式，默认按类型装配
@Autowired
//13、@Qualifier：自动装配bean时按bean名称装配
@Qualifier("bookDao")
//14、@Bean修饰的方法，形参根据类型自动装配 方法的返回值定义为一个bean
@Bean
//15、@EnableTransactionManagement 开启事务支持
@EnableTransactionManagement
//16、配置当前接口方法具有事务
@Transactional
```


## 一、Spring案例
### 1、入门案例
1. 导入spring的坐标spring-context，对应版本是5.2.10.RELEASE

```xml
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
```
2. 配置bean
   <!--bean标签标示配置bean
   id属性标示给bean起名字
   class属性表示给bean定义类型-->

```xml
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl"/>
```
3. 获取IoC容器

4. 获取bean（根据bean配置id获取）

```java
public class App2 {
    public static void main(String[] args) {
        //3.获取IoC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        //4.获取bean（根据bean配置id获取）
        //BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        //bookDao.save();

        BookService bookService = (BookService) ctx.getBean("bookService");
        bookService.save();

    }
}
```
5. 删除业务层中使用new的方式创建的dao对象

6. 提供对应的set方法

```java
public class BookServiceImpl implements BookService {
    //5.删除业务层中使用new的方式创建的dao对象
    private BookDao bookDao;

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
    //6.提供对应的set方法
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
```
7. 配置server与dao的关系

```xml
    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <!--7.配置server与dao的关系-->
        <!--property标签表示配置当前bean的属性
        name属性表示配置哪一个具体的属性
        ref属性表示参照哪一个bean-->
        <property name="bookDao" ref="bookDao"/>
    </bean>
```

###  2、name与scope

```xml
    <!--name:为bean指定别名，别名可以有多个，使用逗号，分号，空格进行分隔-->
    <bean id="bookService" name="service service4 bookEbi"  class="com.itheima.service.impl.BookServiceImpl">
        <property name="bookDao" ref="bookDao"/>
    </bean>

    <!--scope：为bean设置作用范围，可选值为单例singloton，非单例prototype-->
    <bean id="bookDao" name="dao" class="com.itheima.dao.impl.BookDaoImpl" scope="prototype"/>
```

### 3、bean的创建

#### 1.构造方法实例化bean

```xml
    <!--方式一：构造方法实例化bean-->
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl"/>
```

```java
public class BookDaoImpl implements BookDao {
    public BookDaoImpl() {
        System.out.println("book dao constructor is running ....");
    }
    @Override
    public void save() {
        System.out.println("book dao save ...");
    }
}
```

#### 2.使用静态工厂实例化bean

```xml
<!--方式二：使用静态工厂实例化bean-->
<bean id="orderDao" class="com.itheima.factory.OrderDaoFactory" factory-method="getOrderDao"/>
```

```java
public class OrderDaoImpl implements OrderDao {
    @Override
    public void save() {
        System.out.println("order dao save ...");
    }
}
//静态工厂创建对象
public class OrderDaoFactory {
    public static OrderDao getOrderDao(){
        System.out.println("factory setup....");
        return new OrderDaoImpl();
    }
}
```

#### 3.使用实例工厂实例化bean

```xml
<!--方式三：使用实例工厂实例化bean-->
<bean id="userFactory" class="com.itheima.factory.UserDaoFactory"/>
<bean id="userDao" factory-method="getUserDao" factory-bean="userFactory"/>
```

```java
public class UserDaoImpl implements UserDao {

    @Override
    public void save() {
        System.out.println("user dao save ...");
    }
}
//实例工厂创建对象
public class UserDaoFactory {
    public UserDao getUserDao(){
        return new UserDaoImpl();
    }
}
```



#### 4.使用FactoryBean实例化bean

```xml
<!--方式四：使用FactoryBean实例化bean-->
<bean id="userDao" class="com.itheima.factory.UserDaoFactoryBean"/>
```

```java
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("user dao save ...");
    }
}
//FactoryBean创建对象
public class UserDaoFactoryBean implements FactoryBean<UserDao> {
    //代替原始实例工厂中创建对象的方法
    @Override
    public UserDao getObject() throws Exception {
        return new UserDaoImpl();
    }
    @Override
    public Class<?> getObjectType() {
        return UserDao.class;
    }
}
```

### 4、生命周期
- 初始化容器
  1. 创建对象(内存分配)
  2. 执行构造方法
  3. 执行属性注入( set操作)
  4. 执行bean初始化方法
- 使用bean
  1. 执行业务操作
- 关闭/销毁容器
  1. 执行bean销毁方法
```xml
    <!--init-method：设置bean初始化生命周期回调函数-->
    <!--destroy-method：设置bean销毁生命周期回调函数，仅适用于单例对象-->
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl" init-method="init" destroy-method="destory"/>

    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <property name="bookDao" ref="bookDao"/>
    </bean>
```

```java

public class BookDaoImpl implements BookDao {
    @Override
    public void save() {
        System.out.println("book dao save ...");
    }
    /**
     *  表示bean初始化对应的操作
     */
    public void init(){
        System.out.println("init...");
    }
    /**
     * 表示bean销毁前对应的操作
     */
    public void destory(){
        System.out.println("destory...");
    }
}
```

```java
public class AppForLifeCycle {
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        bookDao.save();
        //注册关闭钩子函数，在虚拟机退出之前回调此函数，关闭容器
        //ctx.registerShutdownHook();
        //关闭容器
        ctx.close();
    }
}
```

### 5、依赖注入

#### 1.Set注入

```xml
    <!--注入简单类型-->
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
        <!--property标签：设置注入属性-->
        <!--name属性：设置注入的属性名，实际是set方法对应的名称-->
        <!--value属性：设置注入简单类型数据值-->
        <property name="connectionNum" value="100"/>
        <property name="databaseName" value="mysql"/>
    </bean>

    <bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>

    <!--注入引用类型-->
    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <!--property标签：设置注入属性-->
        <!--name属性：设置注入的属性名，实际是set方法对应的名称-->
        <!--ref属性：设置注入引用类型bean的id或name-->
        <property name="bookDao" ref="bookDao"/>
        <property name="userDao" ref="userDao"/>
    </bean>
```

```java
public class BookDaoImpl implements BookDao {
    private String databaseName;
    private int connectionNum;

    /**
     * setter注入需要提供要注入对象的set方法
     * @param connectionNum
     */
    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }
    /**
     * setter注入需要提供要注入对象的set方法
     * @param databaseName
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    @Override
    public void save() {
        System.out.println("book dao save ..."+databaseName+","+connectionNum);
    }
}

public class BookServiceImpl implements BookService{
    private BookDao bookDao;
    private UserDao userDao;
    /**
     * setter注入需要提供要注入对象的set方法
     */ 
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    /**
     * setter注入需要提供要注入对象的set方法
     */
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
        userDao.save();
    }
}
```

#### 2.构造方法注入

```xml
<!--
    标准书写
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
        根据构造方法参数名称注入
        <constructor-arg name="connectionNum" value="10"/>
        <constructor-arg name="databaseName" value="mysql"/>
    </bean>
    <bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>

    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <constructor-arg name="userDao" ref="userDao"/>
        <constructor-arg name="bookDao" ref="bookDao"/>
    </bean>
-->
<!--
    解决形参名称的问题，与形参名不耦合
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
        根据构造方法参数类型注入
        <constructor-arg type="int" value="10"/>
        <constructor-arg type="java.lang.String" value="mysql"/>
    </bean>
    <bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>

    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <constructor-arg name="userDao" ref="userDao"/>
        <constructor-arg name="bookDao" ref="bookDao"/>
    </bean>-->

    <!--解决参数类型重复问题，使用位置解决参数匹配-->
    <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
        <!--根据构造方法参数位置注入-->
        <constructor-arg index="0" value="mysql"/>
        <constructor-arg index="1" value="100"/>
    </bean>
    <bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>

    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl">
        <constructor-arg name="userDao" ref="userDao"/>
        <constructor-arg name="bookDao" ref="bookDao"/>
    </bean>
```
1. 强制依赖使用构造器进行,使用setter注入有概率不进行注入导致nu11对象出现
2. 可选依赖使用setter注 入进行,灵活性强
3. Spring框架倡导使用构造器 , 第三方框架内部大多数采用构造器注入的形式进行数据初始化,相对严谨
4. 如果有必要可以两者同时使用 ,使用构造器注入完成强制依赖的注入,使用setter注入完成可选依赖的注入
5. 实际开发过程中还要根据实际情况分析,如果受控对象没有提供setter方法就必须使用构造器注入
6. 自己开发的模块推荐使用setter注入

#### 3.自动装配

```xml
    <bean class="com.itheima.dao.impl.BookDaoImpl"/>
    <!--autowire属性：开启自动装配，通常使用按类型装配 byType：按照类型装配 byName：按照名称装配-->
    <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl" autowire="byType"/>
```

```java

public class BookServiceImpl implements BookService{
    private BookDao bookDao;
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}
```

#### 4.集合注入

```java

public class BookDaoImpl implements BookDao {
    private int[] array;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties properties;

    public void setArray(int[] array) { this.array = array; }
    public void setList(List<String> list) { this.list = list; }
    public void setSet(Set<String> set) { this.set = set; }
    public void setMap(Map<String, String> map) {  this.map = map; }
    public void setProperties(Properties properties) { this.properties = properties; }
}
```

```xml
 <bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
        <!--数组注入-->
        <property name="array">
            <array>
                <value>100</value>
                <value>200</value>
                <value>300</value>
            </array>
        </property>
        <!--list集合注入-->
        <property name="list">
            <list>
                <value>itcast</value>
                <value>itheima</value>
                <value>boxuegu</value>
                <value>chuanzhihui</value>
            </list>
        </property>
        <!--set集合注入-->
        <property name="set">
            <set>
                <value>itcast</value>
                <value>itheima</value>
                <value>boxuegu</value>
                <value>boxuegu</value>
            </set>
        </property>
        <!--map集合注入-->
        <property name="map">
            <map>
                <entry key="country" value="china"/>
                <entry key="province" value="henan"/>
                <entry key="city" value="kaifeng"/>
            </map>
        </property>
        <!--Properties注入-->
        <property name="properties">
            <props>
                <prop key="country">china</prop>
                <prop key="province">henan</prop>
                <prop key="city">kaifeng</prop>
            </props>
        </property>
    </bean>
```

### 6、数据源对象管理

```xml
<!--管理DruidDataSource对象-->
<!--druid-->
<bean class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/spring_db"/>
    <property name="username" value="root"/>
    <property name="password" value="root"/>
</bean>
<!--c3p0-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/spring_db"/>
    <property name="user" value="root"/>
    <property name="password" value="root"/>
    <property name="maxPoolSize" value="1000"/>
</bean>

<!--1.开启context命名空间-->
<!--2.使用context空间加载properties文件-->
<!--<context:property-placeholder location="jdbc.properties" system-properties-mode="NEVER"/>-->
<!--<context:property-placeholder location="jdbc.properties,jdbc2.properties" system-properties-mode="NEVER"/>-->
<!--classpath:*.properties  ：设置加载当前工程类路径中的所有properties文件-->
<!--system-properties-mode属性：是否加载系统属性-->
<!--<context:property-placeholder location="*.properties" system-properties-mode="NEVER"/>-->
<!--classpath*:*.properties  ：设置加载当前工程类路径和当前工程所依赖的所有jar包中的所有properties文件-->
<context:property-placeholder location="classpath*:*.properties" system-properties-mode="NEVER"/>

<!--    3.使用属性占位符${}读取properties文件中的属性-->
<!--    说明：idea自动识别${}加载的属性值，需要手工点击才可以查阅原始书写格式-->
<bean class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl">
    <property name="name" value="${username}"/>
</bean>
```

```PROPERTIES
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://127.0.0.1:3306/spring_db
jdbc.username=root
jdbc.password=root
```

### 7、spring获取配置文件与懒加载

```xml
<!--lazy-init：懒加载，Spring延迟加载bean,默认为false,为true时，使用到此bean时才会被加载-->
<bean id="bookDao" class="com.itheima.dao.impl.BookDaoImpl" lazy-init="true"/>
```

```java
//1.加载类路径下的配置文件
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//2.从文件系统下加载配置文件
ApplicationContext ctx = new FileSystemXmlApplicationContext("D:\\workspace\\spring\\spring_10_container\\src\\main\\resources\\applicationContext.xml"); 
```

### 8、核心容器总结

1. BeanFactory是IoC容器的顶层接口，初始化BeanFactory对象时，加载的bean延迟加载
2. ApplicationContext接口是Spring容器的核心接口，初始化时bean立即加载
3. ApplicationContext接口提供基础的bean操作相关方法，通过其他接口扩展其功能
4. ApplicationContext接口常用初始化类
    - ClassPathXmlApplicationContext
    - FileSystemXmlApplicationContext

### 9、注解开发

```java
public class AppForAnnotation {
    public static void main(String[] args) {
        //AnnotationConfigApplicationContext加载Spring配置类初始化Spring容器
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        System.out.println(bookDao);
        //按类型获取bean
        BookService bookService = ctx.getBean(BookService.class);
        System.out.println(bookService);
    }
}

//声明当前类为Spring配置类
@Configuration
//设置bean扫描路径，多个路径书写为字符串数组格式
@ComponentScan({"com.itheima.service","com.itheima.dao"})
//@PropertySource加载properties配置文件
@PropertySource({"jdbc.properties"})
//@Import:导入配置信息
@Import({JdbcConfig.class})
//@EnableTransactionManagement 开启事务支持
@EnableTransactionManagement
public class SpringConfig {
}

//@Component定义bean
//@Component("bookDao")
//@Repository：@Component衍生注解
@Repository("bookDao")
public class BookDaoImpl implements BookDao {
    public void save() {
        System.out.println("book dao save ...");
    }
}

public interface AccountService {
    /**
     * 转账操作
     * @param out 传出方
     * @param in 转入方
     * @param money 金额
     */
    //配置当前接口方法具有事务
    @Transactional
    public void transfer(String out,String in ,Double money);
    
    public void save();
}

//@Component定义bean
//@Component
//@Service：@Component衍生注解
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public void transfer(String out, String in , Double money) {
        accountDao.outMoney(out,money);
        int i = 1/0;
        accountDao.inMoney(in,money);
    }
    
    public void save() {
        System.out.println("book service save ...");
        bookDao.save();
    }
}

@Repository
//@Scope 为bean设置作用范围，可选值为单例singloton：所有对象为同一个对象，非单例prototype：每次新建一个对象
@Scope("singleton")
public class BookDaoImpl implements BookDao {
    //@Value：注入简单类型（无需提供set方法）
    @Value("${name}")
    private String name;
    
    //@Autowired：注入引用类型，自动装配模式，默认按类型装配
    @Autowired
    //@Qualifier：自动装配bean时按bean名称装配
    @Qualifier("bookDao")
    private BookDao bookDao;
    
    @Override
    public void save() {
        System.out.println("book dao save ...");
    }
    
    //@PostConstruct设置bean的初始化方法
    @PostConstruct
    public void init() {
        System.out.println("init ...");
    }
    
    //@PreDestroy设置bean的销毁方法
    @PreDestroy
    public void destroy() {
        System.out.println("destroy ...");
    }

}


//@Configuration
public class JdbcConfig {
    //1.定义一个方法获得要管理的对象
    @Value("com.mysql.jdbc.Driver")
    private String driver;
    @Value("jdbc:mysql://localhost:3306/spring_db")
    private String url;
    @Value("root")
    private String userName;
    @Value("root")
    private String password;
    //2.添加@Bean，表示当前方法的返回值是一个bean
    //@Bean修饰的方法，形参根据类型自动装配
    @Bean
    public DataSource dataSource(BookDao bookDao){
        System.out.println(bookDao);
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

### 10、spring整合mybatis

```java
// 1.创建数据库连接池DataSource
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
//2.使用mybatis配置配置类
public class MybatisConfig {
    //定义bean，SqlSessionFactoryBean，用于产生SqlSessionFactory对象
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        //指定entity层
        ssfb.setTypeAliasesPackage("com.itheima.domain");
        ssfb.setDataSource(dataSource);
        return ssfb;
    }
    //定义bean，返回MapperScannerConfigurer对象
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        //指定dao层
        msc.setBasePackage("com.itheima.dao");
        return msc;
    }
}

//3.spring配置类
@Configuration
@ComponentScan("com.itheima")
//@PropertySource：加载类路径jdbc.properties文件
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class,MybatisConfig.class})
public class SpringConfig {
}

//junit
//设置类运行器
@RunWith(SpringJUnit4ClassRunner.class)
//设置Spring环境对应的配置类
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    //支持自动装配注入bean
    @Autowired
    private AccountService accountService;

    @Test
    public void testFindById(){
        System.out.println(accountService.findById(1));

    }
    @Test
    public void testFindAll(){
        System.out.println(accountService.findAll());
    }
}
```

jdbc.properties

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/spring_db?useSSL=false
jdbc.username=root
jdbc.password=root
```