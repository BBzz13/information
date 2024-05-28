# Lombok注解

## 1、@Data

@Data注解：在JavaBean或类JavaBean中使用，这个注解包含范围最广，它包含getter、setter、NoArgsConstructor注解，即当使用当前注解时，会自动生成包含的所有方法；

## 2、@Getter

@getter注解：在JavaBean或类JavaBean中使用，使用此注解会生成对应的getter方法；

## 3、@Setter

@setter注解：在JavaBean或类JavaBean中使用，使用此注解会生成对应的setter方法；

## 4、@NoArgsConstructor

@NoArgsConstructor注解：在JavaBean或类JavaBean中使用，使用此注解会生成对应的无参构造方法；

- staticName：生成的构造方法是私有的，并且生成一个无参，返回类型为当前对象的静态方法，方法名为 staticName 值。

- onConstructor：列出的所有注解都放在生成的构造方法上，JDK 7 之前的写法是 onConstructor = @__({@Deprecated})，而 JDK 8 之后的写法是 onConstructor_ = {@Deprecated}

- access：设置构造方法的访问修饰符，如果设置了 staticName，那么将设置静态方法的访问修饰符，默认：PUBLIC，共有 PUBLIC、MODULE、PROTECTED、PACKAGE、PRIVATE、NONE，其中 MODULE 是 Java 9 的新特性，而 NONE 表示不生成构造函数也不生成静态方法，即停用注解功能。

- force：可以将所有 final 字段初始化为 0、null、false。默认为 false

## 5、@AllArgsConstructor

@AllArgsConstructor注解：在JavaBean或类JavaBean中使用，使用此注解会生成对应的有参构造方法；

## 6、@ToString

@ToString注解：在JavaBean或类JavaBean中使用，使用此注解会自动重写对应的toStirng方法；

## 7、@EqualsAndHashCode

@EqualsAndHashCode注解：在JavaBean或类JavaBean中使用，使用此注解会自动重写对应的equals方法和hashCode方法；

## 8、@Slf4j

@Slf4j：在需要打印日志的类中使用，当项目中使用了slf4j打印日志框架时使用该注解，会简化日志的打印流程，只需调用info方法即可；

## 9、@Log4j

@Log4j：在需要打印日志的类中使用，当项目中使用了log4j打印日志框架时使用该注解，会简化日志的打印流程，只需调用info方法即可；

## 10、@RequiredArgsConstructor

@RequiredArgsConstructor：生成带有必需参数的构造函数。 必需的参数是最终字段和具有约束的字段，例如@NonNull 。

注意

在使用以上注解需要处理参数时，处理方法如下（以@ToString注解为例，其他注解同@ToString注解）：

@ToString(exclude="column")

意义：排除column列所对应的元素，即在生成toString方法时不包含column参数；



@ToString(exclude={"column1","column2"})

意义：排除多个column列所对应的元素，其中间用英文状态下的逗号进行分割，即在生成toString方法时不包含多个column参数；



@ToString(of="column")

意义：只生成包含column列所对应的元素的参数的toString方法，即在生成toString方法时只包含column参数；；

 

@ToString(of={"column1","column2"})

意义：只生成包含多个column列所对应的元素的参数的toString方法，其中间用英文状态下的逗号进行分割，即在生成toString方法时只包含多个column参数；

##  11、@SneakyThrows

普通Exception类,也就是我们常说的受检异常或者Checked Exception会强制要求抛出它的方法声明throws，调用者必须显示的去处理这个异常。设计的目的是为了提醒开发者处理一些场景中必然可能存在的异常情况。比如网络异常造成IOException。

但是现实大部分情况下的异常，我们都是一路往外抛了事。所以渐渐的java程序员处理Exception的常见手段就是外面包一层RuntimeException，接着往上丢

```java
try{
}catch(Exception e){
throw new RuntimeException(e);
}
```

而Lombok的@SneakyThrows就是为了消除这样的模板代码。
使用注解后不需要担心Exception的处理

```java

 import lombok.SneakyThrows;

public class SneakyThrowsExample implements Runnable {
  @SneakyThrows(UnsupportedEncodingException.class)
  public String utf8ToString(byte[] bytes) {
    return new String(bytes, "UTF-8");
  }
  
  @SneakyThrows
  public void run() {
    throw new Throwable();
  }
}
```

起通过编译器生成真正的代码

```java
import lombok.Lombok;

public class SneakyThrowsExample implements Runnable {
  public String utf8ToString(byte[] bytes) {
    try {
      return new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw Lombok.sneakyThrow(e);
    }
  }
  
  public void run() {
    try {
      throw new Throwable();
    } catch (Throwable t) {
      throw Lombok.sneakyThrow(t);
    }
  }
}
```

