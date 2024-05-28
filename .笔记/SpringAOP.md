AOP的定义：AOP将封装好的对象剖开，找出其中对多个对象产生影响的公共行为，并将其封装为一个可重用的模块，这个模块被命名为“切面”（Aspect），切面将那些与业务无关，却被业务模块共同调用的逻辑提取并封装起来，减少了系统中的重复代码，降低了模块间的耦合度，同时提高了系统的可维护性。

# 1、@Aspect

 **定义切面类，加上@Aspect、@Component注解**

```
@Aspect
@Component
//设置注解执行的顺序
@Order(2)
public class AnnotationAspectTest 
```

![image-20230611211324353](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20230611211324353.png)



# 2、@Pointcut



```java
/**
 * 定义切点,切点为对应controller
 */
@Pointcut("execution(public * com.example.zcs.Aop.controller.*.*(..))")
public void aopPointCut(){
}
```
# 3、Advice 在切入点上执行的增强处理



## @Before

目标方法执行之前。



## @After

目标方法执行完，无论是否出现错误异常，都会执行。



## @AfterReturning

目标方法返回结果时，出现错误异常，不会执行。



## @AfterThrowing

目标方法出现错误异常执行。



## @Around

可以实现以上所有功能。



# 4、Spring 

## JoinPoint

方法中的参数JoinPoint为连接点对象，它可以获取当前切入的方法的参数、代理类等信息，因此可以记录一些信息，验证一些信息等；

### 常用API

| 返回参数 | 方法名 | 功能 |
| ------- | ----- | --- |
| Signature | getSignature() | 获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息 |
| Object[] | getArgs();       | 获取传入目标方法的参数对象 |
| Object | getTarget();       | 获取被代理的对象 |
| Object | getThis();         | 获取代理对象 |

## ProceedingJoinPoint对象
| 返回参数 | 方法名 | 功能 |
| ------- | ----- | --- |
| Object | proceed() throws Throwable  | 执行目标方法  |
| Object | proceed(Object[] var1) throws Throwable | 传入的新的参数去执行目标方法 |

# 5、@annotation(annotationType)

 匹配指定注解为切入点的方法；



# 6、表达式概述

@annotation表示标注了某个注解的所有方法。

## execution()

用于匹配方法执行的连接点

```java
execution(

modifier-pattern?
ret-type-pattern
declaring-type-pattern?
name-pattern(param-pattern)
throws-pattern?

)
```

其中带 **?**号的 **modifiers-pattern?，declaring-type-pattern?，hrows-pattern?**是可选项
**ret-type-pattern,name-pattern, parameters-pattern**是必选项；

- **modifier-pattern?** 修饰符匹配，如**public** 表示匹配公有方法
- **ret-type-pattern** 返回值匹配，***** 表示任何返回值,全路径的类名等
- **declaring-type-pattern?** 类路径匹配
- **name-pattern** 方法名匹配，***** 代表所有,**set\***,代表以**set**开头的所有方法
- (**param-pattern**) 参数匹配，指定方法参数(声明的类型),

​     (..)代表所有参数,

​     ()代表一个参数,

​     (,**String**)代表第一个参数为任何值,第二个为String类型.

- **throws-pattern?** 异常类型匹配

**例子:**

- execution(public * *(..)) 定义任意公共方法的执行
- execution(* set*(..)) 定义任何一个以"set"开始的方法的执行
- execution(* com.xyz.service.AccountService.*(..)) 定义AccountService 接口的任意方法的执行
- execution(* com.xyz.service.*.*(..)) 定义在service包里的任意方法的执行
- execution(* com.xyz.service ..*.*(..)) 定义在service包和所有子包里的任意类的任意方法的执行
- execution(* com.test.spring.aop.pointcutexp…JoinPointObjP2.*(…)) 定义在pointcutexp包和所有子包里的JoinPointObjP2类的任意方法的执行：



## args() 

用于匹配当前执行的方法传入的参数为指定类型的执行方法

## this()

用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；

- **this(com.test.spring.aop.pointcutexp.Intf)** 实现了**Intf**接口的所有类,如果**Intf**不是接口,限定Intf单个类。

## target()

用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配；

- **@target(org.springframework.transaction.annotation.Transactional)** 带有**@Transactional**标注的所有类的任意方法.

## within()

用于匹配指定类型内的方法执行；

- **within(com.test.spring.aop.pointcutexp.\*) pointcutexp**包里的任意类。
- **within(com.test.spring.aop.pointcutexp…\*) pointcutexp**包和所有子包里的任意类。

## @args()

于匹配当前执行的方法传入的参数持有指定注解的执行；

## @target()

用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解；

## @within()

用于匹配所以持有指定注解类型内的方法；

- **@within(org.springframework.transaction.annotation.Transactional)** 带有**@Transactional**标注的所有类的任意方法。

## @annotation

用于匹配当前执行方法持有指定注解的方法；

- **@annotation(org.springframework.transaction.annotation.Transactional)** 带有**@Transactional**标注的任意方法.