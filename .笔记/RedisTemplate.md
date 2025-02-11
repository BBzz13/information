# 一、SpringDataRedis简介

## 1、Redis

redis是一款开源的Key-Value数据库，运行在内存中，由C语言编写。企业开发通常采用Redis来实现缓存。同类的产品还有memcache 、memcached 等。

## 2、Jedis

Jedis是Redis官方推出的一款面向Java的客户端，提供了很多接口供Java语言调用。可以在Redis官网下载，当然还有一些开源爱好者提供的客户端，如Jredis、SRP等等，推荐使用Jedis。

## 3、Spring Data Redis

Spring-data-redis是spring大家族的一部分，提供了在srping应用中通过简单的配置访问redis服务，对reids底层开发包(Jedis, JRedis, and RJC)进行了高度封装，RedisTemplate提供了redis各种操作、异常处理及序列化，支持发布订阅，并对spring 3.1 cache进行了实现。
spring-data-redis针对jedis提供了如下功能：

1. 连接池自动管理，提供了一个高度封装的“RedisTemplate”类

2. 针对jedis客户端中大量api进行了归类封装,将同一类型操作封装为operation接口
   - ValueOperations：简单K-V操作
   - SetOperations：set类型数据操作
   - ZSetOperations：zset类型数据操作
   - HashOperations：针对map类型的数据操作
   - ListOperations：针对list类型的数据操作

3. 提供了对key的“bound”(绑定)便捷化操作API，可以通过bound封装指定的key，然后进行一系列的操作而无须“显式”的再次指定Key，即BoundKeyOperations：
   - BoundValueOperations
   - BoundSetOperations
   - BoundListOperations
   - BoundSetOperations
   - BoundHashOperations

4. 将事务操作封装，有容器控制。

5. 针对数据的“序列化/反序列化”，提供了多种可选择策略(RedisSerializer)

`JdkSerializationRedisSerializer：`POJO对象的存取场景，使用JDK本身序列化机制，将pojo类通过ObjectInputStream/ObjectOutputStream进行序列化操作，最终redis-server中将存储字节序列。是目前最常用的序列化策略。

`StringRedisSerializer：`Key或者value为字符串的场景，根据指定的charset对数据的字节序列编码成string，是“new String(bytes, charset)”和“string.getBytes(charset)”的直接封装。是最轻量级和高效的策略。

`JacksonJsonRedisSerializer：`jackson-json工具提供了javabean与json之间的转换能力，可以将pojo实例序列化成json格式存储在redis中，也可以将json格式的数据转换成pojo实例。因为jackson工具在序列化和反序列化时，需要明确指定Class类型，因此此策略封装起来稍微复杂。【需要jackson-mapper-asl工具支持】

# 二、RedisTemplate中API使用

## 1、pom.xml依赖

```xml
<!--Redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 2、配置文件

```properties
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器地址
spring.redis.host=127.0.0.1
# Redis数据库索引（默认为0）
spring.redis.database=0
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.jedis.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.jedis.pool.max-wait=-1ms
# 连接池中的最大空闲连接
spring.redis.jedis.pool.max-idle=8
# 连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
# 连接超时时间（毫秒）
spring.redis.timeout=5000ms
```



## 3、RedisTemplate的直接方法

**首先使用@Autowired注入RedisTemplate（后面直接使用，就不特殊说明）**

```Java
@Autowired
private RedisTemplate redisTemplate;
```


1、删除单个key

```Java
//    删除key
public void delete(String key){
    redisTemplate.delete(key);
}
```


2、删除多个key

```java
//    删除多个key
public void deleteKey (String ...keys){
    redisTemplate.delete(keys);
}
```

3、指定key的失效时间

```Java
//    指定key的失效时间
public void expire(String key,long time){
    redisTemplate.expire(key,time,TimeUnit.MINUTES);
}
```


4、根据key获取过期时间

```Java
//    根据key获取过期时间
public long getExpire(String key){
    Long expire = redisTemplate.getExpire(key);
    return expire;
}
```


5、判断key是否存在

```Java
//    判断key是否存在
public boolean hasKey(String key){
    return redisTemplate.hasKey(key);
}
```

## 4、String类型相关操作

1)、添加缓存（2/3是1的递进值）

```Java
//1、通过redisTemplate设置值
redisTemplate.boundValueOps("StringKey").set("StringValue");
redisTemplate.boundValueOps("StringKey").set("StringValue",1, TimeUnit.MINUTES);
//2、通过BoundValueOperations设置值
BoundValueOperations stringKey = redisTemplate.boundValueOps("StringKey");
stringKey.set("StringVaule");
stringKey.set("StringValue",1, TimeUnit.MINUTES);
//3、通过ValueOperations设置值	
ValueOperations ops = redisTemplate.opsForValue();
ops.set("StringKey", "StringVaule");
ops.set("StringValue","StringVaule",1, TimeUnit.MINUTES);
```

2)、设置过期时间(单独设置)

```Java
redisTemplate.boundValueOps("StringKey").expire(1,TimeUnit.MINUTES);
redisTemplate.expire("StringKey",1,TimeUnit.MINUTES);
```

3)、获取缓存值（2/3是1的递进值）

```Java
//1、通过redisTemplate设置值
String str1 = (String) redisTemplate.boundValueOps("StringKey").get();

//2、通过BoundValueOperations获取值
BoundValueOperations stringKey = redisTemplate.boundValueOps("StringKey");
String str2 = (String) stringKey.get();

//3、通过ValueOperations获取值
ValueOperations ops = redisTemplate.opsForValue();
String str3 = (String) ops.get("StringKey");
```

4)、删除key

```Java
Boolean result = redisTemplate.delete("StringKey");
```

5)、顺序递增

```java
redisTemplate.boundValueOps("StringKey").increment(3L);
```

6)、顺序递减

```java
redisTemplate.boundValueOps("StringKey").increment(-3L);
```

## 5、Hash类型相关操作

1)、添加缓存（2/3是1的递进值）

```java
//1、通过redisTemplate设置值
redisTemplate.boundHashOps("HashKey").put("SmallKey", "HashVaue");

//2、通过BoundValueOperations设置值
BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
hashKey.put("SmallKey", "HashVaue");

//3、通过ValueOperations设置值
HashOperations hashOps = redisTemplate.opsForHash();
hashOps.put("HashKey", "SmallKey", "HashVaue");
```

2)、设置过期时间(单独设置)

```java
redisTemplate.boundValueOps("HashKey").expire(1,TimeUnit.MINUTES);
redisTemplate.expire("HashKey",1,TimeUnit.MINUTES);
```

3)、添加一个Map集合

```java
HashMap<String, String> hashMap = new HashMap<>();
redisTemplate.boundHashOps("HashKey").putAll(hashMap );
```

4)、设置过期时间(单独设置)

```java
redisTemplate.boundValueOps("HashKey").expire(1,TimeUnit.MINUTES);
redisTemplate.expire("HashKey",1,TimeUnit.MINUTES);
```

5)、提取所有的小key

```java
//1、通过redisTemplate获取值
Set keys1 = redisTemplate.boundHashOps("HashKey").keys();

//2、通过BoundValueOperations获取值
BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
Set keys2 = hashKey.keys();

//3、通过ValueOperations获取值
HashOperations hashOps = redisTemplate.opsForHash();
Set keys3 = hashOps.keys("HashKey");
```

6)、提取所有的value值

```java
//1、通过redisTemplate获取值
List values1 = redisTemplate.boundHashOps("HashKey").values();

//2、通过BoundValueOperations获取值
BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
List values2 = hashKey.values();

//3、通过ValueOperations获取值
HashOperations hashOps = redisTemplate.opsForHash();
List values3 = hashOps.values("HashKey");
```

7)、根据key提取value值

```java
//1、通过redisTemplate获取
String value1 = (String) redisTemplate.boundHashOps("HashKey").get("SmallKey");

//2、通过BoundValueOperations获取值
BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
String value2 = (String) hashKey.get("SmallKey");

//3、通过ValueOperations获取值
HashOperations hashOps = redisTemplate.opsForHash();
String value3 = (String) hashOps.get("HashKey", "SmallKey");
```

8)、获取所有的键值对集合

```java
//1、通过redisTemplate获取
Map entries = redisTemplate.boundHashOps("HashKey").entries();

//2、通过BoundValueOperations获取值
BoundHashOperations hashKey = redisTemplate.boundHashOps("HashKey");
Map entries1 = hashKey.entries();

//3、通过ValueOperations获取值
HashOperations hashOps = redisTemplate.opsForHash();
Map entries2 = hashOps.entries("HashKey");
```

9)、删除

```java
//删除小key
redisTemplate.boundHashOps("HashKey").delete("SmallKey");
//删除大key
redisTemplate.delete("HashKey");
```

10)、判断Hash中是否含有该值

```java
Boolean isEmpty = redisTemplate.boundHashOps("HashKey").hasKey("SmallKey");
```

## 6、Set类型相关操作

1)、添加Set缓存(值可以是一个，也可是多个)(2/3是1的递进值）

```java
//1、通过redisTemplate设置值
redisTemplate.boundSetOps("setKey").add("setValue1", "setValue2", "setValue3");

//2、通过BoundValueOperations设置值
BoundSetOperations setKey = redisTemplate.boundSetOps("setKey");
setKey.add("setValue1", "setValue2", "setValue3");

//3、通过ValueOperations设置值
SetOperations setOps = redisTemplate.opsForSet();
setOps.add("setKey", "SetValue1", "setValue2", "setValue3");
```

2)、设置过期时间(单独设置)

```java
redisTemplate.boundValueOps("setKey").expire(1,TimeUnit.MINUTES);
redisTemplate.expire("setKey",1,TimeUnit.MINUTES);
```

3)、根据key获取Set中的所有值

```java
//1、通过redisTemplate获取值
Set set1 = redisTemplate.boundSetOps("setKey").members();

//2、通过BoundValueOperations获取值
BoundSetOperations setKey = redisTemplate.boundSetOps("setKey");
Set set2 = setKey.members();

//3、通过ValueOperations获取值
SetOperations setOps = redisTemplate.opsForSet();
Set set3 = setOps.members("setKey");
```

4)、根据value从一个set中查询,是否存在

```java
Boolean isEmpty = redisTemplate.boundSetOps("setKey").isMember("setValue2");
```

5)、获取Set缓存的长度

```java
Long size = redisTemplate.boundSetOps("setKey").size();
```

6)、移除指定的元素

```java
Long result1 = redisTemplate.boundSetOps("setKey").remove("setValue1");
```

7)、移除指定的key

```java
Boolean result2 = redisTemplate.delete("setKey");
```

## 7、 LIST类型相关操作

1)、添加缓存（2/3是1的递进值）

```java
//1、通过redisTemplate设置值
redisTemplate.boundListOps("listKey").leftPush("listLeftValue1");
redisTemplate.boundListOps("listKey").rightPush("listRightValue2");

//2、通过BoundValueOperations设置值
BoundListOperations listKey = redisTemplate.boundListOps("listKey");
listKey.leftPush("listLeftValue3");
listKey.rightPush("listRightValue4");

//3、通过ValueOperations设置值
ListOperations opsList = redisTemplate.opsForList();
opsList.leftPush("listKey", "listLeftValue5");
opsList.rightPush("listKey", "listRightValue6");
```

2)、将List放入缓存

```java
ArrayList<String> list = new ArrayList<>();
redisTemplate.boundListOps("listKey").rightPushAll(list);
redisTemplate.boundListOps("listKey").leftPushAll(list);
```

3)、设置过期时间(单独设置)

```java
redisTemplate.boundValueOps("listKey").expire(1,TimeUnit.MINUTES);
redisTemplate.expire("listKey",1,TimeUnit.MINUTES);
```

4)、获取List缓存全部内容（起始索引，结束索引）

```java
List listKey1 = redisTemplate.boundListOps("listKey").range(0, 10); 
```

5)、从左或从右弹出一个元素

```java
String listKey2 = (String) redisTemplate.boundListOps("listKey").leftPop();  //从左侧弹出一个元素
String listKey3 = (String) redisTemplate.boundListOps("listKey").rightPop(); //从右侧弹出一个元素
```

6)、根据索引查询元素

```java
String listKey4 = (String) redisTemplate.boundListOps("listKey").index(1);
```

7)、获取List缓存的长度

```java
Long size = redisTemplate.boundListOps("listKey").size();
```

8)、根据索引修改List中的某条数据(key，索引，值)

```java
redisTemplate.boundListOps("listKey").set(3L,"listLeftValue3");
```

9)、移除N个值为value(key,移除个数，值)

```java
redisTemplate.boundListOps("listKey").remove(3L,"value");
```

## 8、Zset类型的相关操作

1)、向集合中插入元素，并设置分数

```java
//1、通过redisTemplate设置值
redisTemplate.boundZSetOps("zSetKey").add("zSetVaule", 100D);

//2、通过BoundValueOperations设置值
BoundZSetOperations zSetKey = redisTemplate.boundZSetOps("zSetKey");
zSetKey.add("zSetVaule", 100D);

//3、通过ValueOperations设置值
ZSetOperations zSetOps = redisTemplate.opsForZSet();
zSetOps.add("zSetKey", "zSetVaule", 100D);
```

2)、向集合中插入多个元素,并设置分数

```java
DefaultTypedTuple<String> p1 = new DefaultTypedTuple<>("zSetVaule1", 2.1D);
DefaultTypedTuple<String> p2 = new DefaultTypedTuple<>("zSetVaule2", 3.3D);
redisTemplate.boundZSetOps("zSetKey").add(new HashSet<>(Arrays.asList(p1,p2)));
```

3)、按照排名先后(从小到大)打印指定区间内的元素, -1为打印全部

```java
Set<String> range = redisTemplate.boundZSetOps("zSetKey").range(0, -1);
```

4)、获得指定元素的分数

```java
Double score = redisTemplate.boundZSetOps("zSetKey").score("zSetVaule");
```

5)、返回集合内的成员个数

```java
Long size = redisTemplate.boundZSetOps("zSetKey").size();
```

6)、返回集合内指定分数范围的成员个数（Double类型）

```java
Long COUNT = redisTemplate.boundZSetOps("zSetKey").count(0D, 2.2D);
```

7)、返回集合内元素在指定分数范围内的排名（从小到大）

```java
Set byScore = redisTemplate.boundZSetOps("zSetKey").rangeByScore(0D, 2.2D);
```

8)、带偏移量和个数，(key，起始分数，最大分数，偏移量，个数)

```java
Set<String> ranking2 = redisTemplate.opsForZSet().rangeByScore("zSetKey", 0D, 2.2D 1, 3);
```

9)、返回集合内元素的排名，以及分数（从小到大）

```java
Set<TypedTuple<String>> tuples = redisTemplate.boundZSetOps("zSetKey").rangeWithScores(0L, 3L);
  for (TypedTuple<String> tuple : tuples) {
      System.out.println(tuple.getValue() + " : " + tuple.getScore());
  }
```

10)、返回指定成员的排名

```java
//从小到大
Long startRank = redisTemplate.boundZSetOps("zSetKey").rank("zSetVaule");
//从大到小
Long endRank = redisTemplate.boundZSetOps("zSetKey").reverseRank("zSetVaule");
```

11)、从集合中删除指定元素

```java
redisTemplate.boundZSetOps("zSetKey").remove("zSetVaule");
```

12)、删除指定索引范围的元素（Long类型）

```java
redisTemplate.boundZSetOps("zSetKey").removeRange(0L,3L);
```

13)、删除指定分数范围内的元素（Double类型）

```java
redisTemplate.boundZSetOps("zSetKey").removeRangeByScorssse(0D,2.2D);
```

14)、为指定元素加分（Double类型）

```java
Double score = redisTemplate.boundZSetOps("zSetKey").incrementScore("zSetVaule",1.1D);
```

