# 1、搭建Eureka服务中心

## 1.1、Maven坐标

```xml
<!--Eureka注册中心-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

<!-- eureka发现服务 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## 1.2 Eureka 配置文件

````yml
server:
  port: 8761

eureka:
  client:
    #healthcheck: true # 开启健康检查(依赖spring-boot-actuator)
    service-url:
      defaultZone: http://eureka:eureka@127.0.0.1:${server.port}/eureka/ #客户端与Eureka服务端进行交互的地址
    register-with-eureka: false    #是否将自己注册到Eureka服务中，本身就是所有无需注册
    fetch-registry: false          #是否从Eureka中获取注册信息
  instance:
    prefer-ip-address: true #是否注册ip地址
    ip-address: 127.0.0.1   # ip地址
#  server:
#    enable-self-preservation: false     #关闭自我保护
#    eviction-interval-timer-in-ms: 4000 #剔除时间间隔,单位:毫秒
#    renewal-percent-threshold: 0.9  # 表示Eureka Server开启自我保护的系数，默认：0.85。
                                     # Renews threshold = 4 * 2 * 0.85 = 6.8（取整为：6）
                                     # Renews (last min) = 4 * 2 = 8
                                     # Renews 默认 15 分钟刷新一次，可以通过设计以下值进行调整：
# docker-compose部署时候 hostname 换成vtrace-eureka
# 类似的 redis 使用vtrace-redis ,gateway 换成 vtrace-gateway
````

