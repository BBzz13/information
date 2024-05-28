# 1、SpringCloud Config

## 1.1 什么是配置中心

### 1.1.1配置中心概述

对于传统的单体应用而言，常使用配置文件来管理所有配置，比如SpringBoot的application.yml文件，但是在微服务架构中全部手动修改的话很麻烦而且不易维护。微服务的配置管理一般有以下需求：

- 集中配置管理，一个微服务架构中可能有成百上千个微服务，所以集中配置管理是很重要的。

- 不同环境不同配置，比如数据源配置在不同环境（开发，生产，测试）中是不同的。

- 运行期间可动态调整。例如，可根据各个微服务的负载情况，动态调整数据源连接池大小等

- 配置修改后可自动更新。如配置内容发生变化，微服务可以自动更新配置

综上所述对于微服务架构而言，一套统一的，通用的管理配置机制是不可缺少的总要组成部分。常见的做法就是通过配置服务器进行管理。

### 1.1.2 常见配置中心

**Spring Cloud Confifig**为分布式系统中的外部配置提供服务器和客户端支持。

**Apollo**（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于微服务配置管理场景。

**Disconf** 专注于各种「分布式系统配置管理」的「通用组件」和「通用平台」, 提供统一的「配置管理服务」包括 百度、滴滴出行、银联、网易、拉勾网、苏宁易购、顺丰科技 等知名互联网公司正在使用! 「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。

## 1.2 Spring Cloud Config简介

Spring Cloud Confifig项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用。

![image-20221104155653644](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104155653644.png)

Spring Cloud Confifig为分布式系统中的外部配置提供服务器和客户端支持。使用Confifig Server，您可以为所有环境中的应用程序管理其外部属性。它非常适合spring应用，也可以使用在其他语言的应用上。随着应用程序通过从开发到测试和生产的部署流程，您可以管理这些环境之间的配置，并确定应用程序具有迁移时需要运行的一切。服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。

Spring Cloud Confifig服务端特性：

- HTTP，为外部配置提供基于资源的API（键值对，或者等价的YAML内容）

- 属性值的加密和解密（对称加密和非对称加密）

- 通过使用@EnableConfifigServer在Spring boot应用中非常简单的嵌入。

 Confifig客户端的特性（特指Spring应用）

- 绑定Confifig服务端，并使用远程的属性源初始化Spring环境。

- 属性值的加密和解密（对称加密和非对称加密）

## 1.3 Spring Cloud Confifig入门

### 1.3.1 准备工作

Confifig Server是一个可横向扩展、集中式的配置服务器，它用于集中管理应用程序各个环境下的配置，默认使用Git存储配置文件内容，也可以使用SVN存储，或者是本地文件存储。这里使用git作为学习的环境。

使用GitHub时，国内的用户经常遇到的问题是访问速度太慢，有时候还会出现无法连接的情况。如果我们希望体验Git飞一般的速度，可以使用国内的Git托管服务——码云（gitee.com）。和GitHub相比，码云也提供免费的Git仓库。此外，还集成了代码质量检测、项目演示等功能。对于团队协作开发，码云还提供了项目管理、代码托管、文档管理的服务。

（1）浏览器打开gitee.com，注册用户 ，注册后登陆码云管理控制台

![image-20221104160029928](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104160029928.png)

（2）创建项目**confifig-repo**

（3）上传配置文件，将product_service工程的application.yml改名为product-dev.yml后上传。

![image-20221104160052244](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104160052244.png)

文件命名规则：

- {application}-{profifile}.yml

- {application}-{profifile}.properties

- application为应用名称 profifile指的开发环境（用于区分开发环境，测试环境、生产环境等）

### 1.3.2 搭建服务程序

**（1）引入依赖**

```xml
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**（2）配置启动类**

```java
@SpringBootApplication
@EnableConfigServer //开启配置中心服务端功能
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

- @EnableConfifigServer : 通过此注解开启注册中心服务端功能

**（3）配置application.yml**

```yml
server:
	port: 10000 #服务端口
spring:
	application:
    	name: config-server #指定服务名
 cloud:
 	config:
     	server:
       		git:
         		uri: https://gitee.com/it-lemon/config-repo.git
```

- 通过 spring.cloud.config.server.git.uri : 配置git服务地址

- 通过`spring.cloud.confifig.server.git.username: 配置git用户名

- 通过`spring.cloud.confifig.server.git.password: 配置git密码

**（4）测试**

启动此微服务，可以在浏览器上，通过server端访问到git服务器上的文件

![image-20221104160441224](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104160441224.png)

### 2.3.3 修改客户端程序

**（1）引入依赖**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**（2）删除application.yml**

springboot的应用配置文件，需要通过Confifig-server获取，这里不再需要。

**（3）添加bootstrap.yml**

使用加载级别更高的 bootstrap.yml 文件进行配置。启动应用时会检查此配置文件，在此文件中指定配置中心的服务地址。会自动的拉取所有应用配置并启用。

```yml
spring:
     cloud:
     	config:
            name: product
            profile: dev
            label: master
            uri: http://localhost:8080
```



### 1.3.4手动刷新

我们已经在客户端取到了配置中心的值，但当我们修改GitHub上面的值时，服务端（Confifig Server）能实时获取最新的值，但客户端（Confifig Client）读的是缓存，无法实时获取最新值。SpringCloud已经为我们解决了这个问题，那就是客户端使用post去触发refresh，获取最新数据，需要依赖spring-boot-starter-actuator

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

对应的controller类加上@RefreshScope

```java
@RefreshScope
@RestController
public class TestController{
    @Value("${productValue}")
    private String productValue;
    /**
     * 访问首页
     */
    @GetMapping("/index")
    public String index(){
        return "hello springboot！productValue：" + productValue;
   }
}
```

配置文件中开发端点

```yml
management:
 endpoints:
   web:
     exposure:
       include: /bus-refresh
```

在postman中访问http://localhost:9002/actuator/bus-refresh,使用post提交,查看数据已经发生了变化。

## 1.4 配置中心的高可用

在之前的代码中，客户端都是直接调用配置中心的server端来获取配置文件信息。这样就存在了一个问题，客户端和服务端的耦合性太高，如果server端要做集群，客户端只能通过原始的方式来路由，server端改变IP地址的时候，客户端也需要修改配置，不符合springcloud服务治理的理念。

springcloud提供了这样的解决方案，我们只需要将server端当做一个服务注册到eureka中，client端去eureka中去获取配置中心server端的服务既可。

### 1.4.1 服务端改造

**（1）添加依赖**

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
</dependencies>
```

**（2）配置文件**

```yml
eureka:
 client:
   serviceUrl:
     defaultZone: http://localhost:8000/eureka/   ## 注册中心eurka地址
```

这样server端的改造就完成了。先启动eureka注册中心，在启动server端，在浏览器中访问：

http://localhost:8761/ 就会看到server端已经注册了到注册中心了。

### 1.4.2 服务端改造

**（1）添加依赖**

```xml
<dependencies>
 <dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-starter-config</artifactId>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
 <dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-starter-eureka</artifactId>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-test</artifactId>
 <scope>test</scope>
 </dependency>
</dependencies>
```

**（2）配置文件**

```yml
server:
 port: 9002

eureka:
 client:
   serviceUrl:
     defaultZone: http://127.0.0.1:8761/eureka/
spring:
 cloud:
   config:
     name: product
     profile: dev
     label: master
     uri: http://localhost:8080
     discovery:
       enabled: true #从eureka中获取配置中心信息
       service-id: config-server
```

### 2.4.3高可用

为了模拟生产集群环境，我们改动server端的端口为1000，再启动一个server端来做服务的负载，提供高可用的server端支持。

如上图就可发现会有两个server端同时提供配置中心的服务，防止某一台down掉之后影响整个系统的使用。

我们先单独测试服务端，分别访问： http://localhost:10000/product-pro.yml 

http://localhost:10001/product-pro.yml 返回信息：

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://127.0.0.1:8761/eureka/
  instance:
    instance-id: ${spring.cloud.client.ip-address}:9002
    preferIpAddress: true
productValue: 200
server:
  port: 9002
spring:
  application:
    name: shop-service-product
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    password: 111111
    url: jdbc:mysql://localhost:3306/shop?useUnicode=true&characterEncoding=utf8
    username: root
  jpa:
    database: MySQL
    open-in-view: true
    show-sql: true
```

## 1.5消息总线bus

在微服务架构中，通常会使用轻量级的消息代理来构建一个共用的消息主题来连接各个微服务实例，它广播的消息会被所有在注册中心的微服务实例监听和消费，也称消息总线。

SpringCloud中也有对应的解决方案，SpringCloud Bus 将分布式的节点用轻量的消息代理连接起来，可以很容易搭建消息总线，配合SpringCloud confifig 实现微服务应用配置信息的动态更新。

![image-20221104164919663](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104164919663.png)

根据此图我们可以看出利用Spring Cloud Bus做配置更新的步骤:

- 提交代码触发post请求给bus/refresh

- server端接收到请求并发送给Spring Cloud Bus

- Spring Cloud bus接到消息并通知给其它客户端

- 其它客户端接收到通知，请求Server端获取最新配置

- 全部客户端均获取到最新的配置

## 1.6 消息总线整合配置中心

**（1）添加依赖**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-bus</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
</dependency>
```

**（2）服务端配置**

```yml
server:
 port: 10000 #服务端口
spring:
 application:
   name: config-server #指定服务名
 cloud:
   config:
     server:
       git:
         uri: https://gitee.com/it-lemon/config-repo.git
 rabbitmq:
   host: 127.0.0.1
   port: 5672
   username: guest
   password: guest
management:
 endpoints:
   web:
     exposure:
       include: bus-refresh
eureka:
 client:
   serviceUrl:
     defaultZone: http://127.0.0.1:8761/eureka/
 instance:
   preferIpAddress: true
   instance-id: ${spring.cloud.client.ip-address}:${server.port}
#spring.cloud.client.ip-address:获取ip地址
```

**（3）微服务客户端配置**

```yml
server:
 port: 9002
eureka:
 client:
   serviceUrl:
     defaultZone: http://127.0.0.1:8761/eureka/
spring:
 cloud:
   config:
     name: product
     profile: dev
     label: master
     discovery:
       enabled: true
       service-id: config-serve
```

需要在码云对应的配置文件中添加rabbitmq的配置信息

![image-20221104165249952](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221104165249952.png)

重新启动对应的eureka-server ， confifig-server ， product-service。配置信息刷新后，只需要向配置中心发送对应的请求，即可刷新每个客户端的配置

# 2、SpringCloud Config 配置文件解析

## 2.1、Config -Service

```yml
spring:
  application:
    name: yu-config
  profiles:
    active: dev
  # 配置中心
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config/
  # 配置中心
#  cloud:
#    config:
#      server:
#        git:
#          uri: http://58.33.9.130:30071/vtrace-platform/vtrace-config.git #git仓库地址
#          search-paths: uat  #分支
#          username: jenkins  #用户名
#          password: Onechain001  #密码
```

## 2.2、Config -Client

```yml
spring:
  profiles:
    active: dev
  application:
    name: yu-org
  cloud:
    config:
      fail-fast: true  #是否启动快速失败功能，功能开启则优先判断config server是否正常
      name: ${spring.application.name}  #配置文件的名称
      profile: ${spring.profiles.active}  # 配置文件的后缀
      discovery:
        enabled: true #开启配置信息发现
        service-id: config-service  #指定配置中心的service-id，便于扩展为高可用配置集群。
```