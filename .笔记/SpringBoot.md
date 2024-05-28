# 1、SpringBoot注释

## 1.1、@ConditionalOnBean(Xxxx.class)

仅仅在当前上下文中存在Xxxx对象时，才会实例化一个Bean 。
也就是只有当Xxxx.class 在spring的**applicationContext**中存在时，这个当前的bean才能够创建。

## 1.2、@ConditionalOnMissingBean

仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean。

## 1.3、@ConditionalOnClass

某个class位于类路径上，才会实例化一个Bean，@ConditionalOnClass({JWT.class, DefaultWebSecurityManager.class})

## 1.4、@AutoConfigureAfter

在加载参数类之后再加载当前类 有时配合着@import 注解使用 ，@AutoConfigureAfter(WebMvcAutoConfiguration.class)

## 1.5、@EnableConfigurationProperties

@EnableConfigurationProperties 注解的作用是:让使用了 @ConfigurationProperties 注解的类生效,并且将该类注入到 IOC 容器中,交由 IOC 容器进行管理

## 1.6、@ResponseStatus

@ResponseStatus有两个参数

**value**：对应枚举HttpStatus的值，此值对应相应404,403,500

**reason**：界面提示文字

# 2、异常抛出和返回

#### 1、创建一个异常继承**RuntimeException**类

```java
/**
 * @author liuyu
 * @date 2022年11月9日17:34:01
 */
    public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CustomException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CustomException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CustomException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}

```

#### 2、代码中如果需要异常直接抛出，如下：

```java
    @GetMapping("/{id}")
    public R<Book> getById(@PathVariable("id") Integer id)  {
        if (true) {
            //使用throw手动抛出异常
            throw new CustomException("手动抛出异常");
        }
        return R.ok(bookService.getById(id));
    }

```

此时代码走到此处如果有问题就可以把一场抛出，在控制台/日志中打印出来。

![image-20221109212356688](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221109212356688.png)

#### 3、把报错数据捕获后返回

此时可以手动捕获到报错了，但是还需要告诉前端因为什么原因报错，前端可以把报错展示给用户看，以用来调整自己的数。

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private MessageSource messageSource;

    /**
     * 处理自定义异常
     */
    //捕获异常的名字，自己创建的异常名字什么，这里写什么，这样就可以在自己手动抛出异常时此处就可以捕获到异常
    @ExceptionHandler(CustomException.class)
    //http默认状态码 HttpStatus Http状态码枚举 INTERNAL_SERVER_ERROR为500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handleCustomException(CustomException e) {
        //打印message中的内容
        log.error("CustomException ex={}", e.getMessage(), e);
        //获取抛出异常的code 默认为500
        int code = e.getCode();
        try {
            //获取自己写的异常原因
            String msg = e.getMsg();
            if (msg != null) {
                //不为空，进入,创建返回的参数类
                R<String> r = new R<>();
                //添加code
                r.setCode(code);
                //添加msg
                r.setMsg(msg);
                return r;
            }
        } catch (NoSuchMessageException ex) {
            log.warn("CustomException no message defined", ex);
        }
        return  R.error();
    }



    /**
     * 全局异常.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.error();
    }

    /**
     * validation Exception
     * 
     * @param exception 报错原因
     * @return R
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> bodyValidExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        R<String> result = new R<>();
        String msg = null;
        try {
            String messageKey = fieldErrors.get(0).getDefaultMessage();
            if (messageKey != null && messageKey.startsWith("{")) {
                messageKey = messageKey.replace("{", "");
                messageKey = messageKey.replace("}", "");
                msg = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
            }
        } catch (NoSuchMessageException ex) {
            log.warn("validation exception no message defined", ex);
        }
        if (msg == null) {
            msg = fieldErrors.get(0).getDefaultMessage();
        }
        result.setMsg(msg);
        log.warn(fieldErrors.get(0).getDefaultMessage());
        return result;
    }
}
```

效果展示：

![image-20221109214317057](C:\Users\Yue\AppData\Roaming\Typora\typora-user-images\image-20221109214317057.png)



# 3、WebMvcConfigurer详解

WebMvcConfigurer配置类其实是`Spring`内部的一种配置方式，采用`JavaBean`的形式来代替传统的`xml`配置文件形式进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter。基于java-based方式的spring mvc配置，需要创建一个**配置**类并实现**`WebMvcConfigurer`** 接口；

**接口源码**

```java
public interface WebMvcConfigurer {
    void configurePathMatch(PathMatchConfigurer var1);
 
    void configureContentNegotiation(ContentNegotiationConfigurer var1);
 
    void configureAsyncSupport(AsyncSupportConfigurer var1);
 
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer var1);
 
    void addFormatters(FormatterRegistry var1);
 
    void addInterceptors(InterceptorRegistry var1);
 
    void addResourceHandlers(ResourceHandlerRegistry var1);
 
    void addCorsMappings(CorsRegistry var1);
 
    void addViewControllers(ViewControllerRegistry var1);
 
    void configureViewResolvers(ViewResolverRegistry var1);
 
    void addArgumentResolvers(List<HandlerMethodArgumentResolver> var1);
 
    void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> var1);
 
    void configureMessageConverters(List<HttpMessageConverter<?>> var1);
 
    void extendMessageConverters(List<HttpMessageConverter<?>> var1);
 
    void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> var1);
 
    void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> var1);
 
    Validator getValidator();
 
    MessageCodesResolver getMessageCodesResolver();
}
```

**常用的方法：**

```java
/* 拦截器配置 */
void addInterceptors(InterceptorRegistry var1);
/* 视图跳转控制器 */
void addViewControllers(ViewControllerRegistry registry);
/* 静态资源处理 */
void addResourceHandlers(ResourceHandlerRegistry registry);
/* 默认静态资源处理器 */
void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);
/* 这里配置视图解析器 */
void configureViewResolvers(ViewResolverRegistry registry);
/* 配置内容裁决的一些选项*/
void configureContentNegotiation(ContentNegotiationConfigurer configurer);
/* 解决跨域问题 */
public void addCorsMappings(CorsRegistry registry) ;
```

### 3.1 addInterceptors：拦截器

- addInterceptor：需要一个实现HandlerInterceptor接口的拦截器实例
- addPathPatterns：用于设置拦截器的过滤路径规则；`addPathPatterns("/**")`对所有请求都拦截
- excludePathPatterns：用于设置不需要拦截的过滤规则
- 拦截器主要用途：进行用户登录状态的拦截，日志的拦截等。

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);
    registry.addInterceptor(new TestInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/emp/toLogin","/emp/login","/js/**","/css/**","/images/**");
}
```

### InterceptorRegistry

1. **注册拦截器：**InterceptorRegistry 类提供了 addInterceptor 方法，可以注册一个或多个拦截器，用于拦截和处理请求。拦截器可以是实现了 HandlerInterceptor 接口的任意对象，例如身份验证拦截器、日志记录拦截器等。
2. **配置拦截器的拦截路径：**InterceptorRegistry 类提供了 addPathPatterns 方法，可以配置拦截器的拦截路径，即拦截哪些请求。可以使用 Ant 样式的 URL 路径模式来配置拦截路径，例如 /admin/** 表示拦截所有以 /admin/开头的请求。
3. **配置拦截器的排除路径：**InterceptorRegistry 类提供了 excludePathPatterns 方法，可以配置拦截器的排除路径，即哪些请求不需要被拦截。可以使用 Ant 样式的 URL 路径模式来配置排除路径，例如 /public/** 表示不拦截所有以 /public/ 开头的请求。
4. **配置拦截器的执行顺序：**InterceptorRegistry 类提供了 order 方法，可以配置拦截器的执行顺序。拦截器的执行顺序按照注册的顺序决定，可以使用 order 方法指定拦截器的执行顺序，数值越小的拦截器越先执行。
5. **注册默认拦截器：**InterceptorRegistry 类提供了 addInterceptor 方法的重载方法，可以注册 Spring Web 框架提供的默认拦截器，例如 LocaleChangeInterceptor、ThemeChangeInterceptor 等。

### HandlerInterceptor

**HandlerInterceptor拦截器 ：**拦截请求地址的拦截器

**源码**

```java
public interface HandlerInterceptor {

	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	
	default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}
```

- `preHandler：`目标方法执行完成之前
- postHanlder：目标方法执行完成之后
- afterComplete：页面渲染以后

**MethodInterceptor接口：**拦截方法的拦截器

**源码**

```java
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation var1) throws Throwable;
}
```






### 3.2 addViewControllers：页面跳转

以前写SpringMVC的时候，如果需要访问一个页面，必须要写Controller类，然后再写一个方法跳转到页面，感觉好麻烦，其实重写WebMvcConfigurer中的addViewControllers方法即可达到效果了

```java
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/toLogin").setViewName("login");
}
```

值的指出的是，在这里重写addViewControllers方法，并不会覆盖**WebMvcAutoConfiguration**（Springboot自动配置）中的addViewControllers（在此方法中，Spring Boot将“/”映射至index.html），这也就意味着自己的配置和Spring Boot的自动配置同时有效，这也是我们推荐添加自己的MVC配置的方式。

 

### 3.3 addResourceHandlers：静态资源

比如，我们想自定义静态资源映射目录的话，只需重写addResourceHandlers方法即可。

注：如果继承WebMvcConfigurationSupport类实现配置时必须要重写该方法，具体见其它文章

```java
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {
    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/my/**").addResourceLocations("classpath:/my/");
    }
}
```

- addResoureHandler：指的是对外暴露的访问路径
- addResourceLocations：指的是内部文件放置的目录

### 3.4 configureDefaultServletHandling：默认静态资源处理器

```java
@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
        configurer.enable("defaultServletName");
}
```

此时会注册一个默认的[Handler](https://so.csdn.net/so/search?q=Handler&spm=1001.2101.3001.7020)：DefaultServletHttpRequestHandler，这个Handler也是用来处理静态文件的，它会尝试映射/。当DispatcherServelt映射/时（/ 和/ 是有区别的），并且没有找到合适的Handler来处理请求时，就会交给DefaultServletHttpRequestHandler 来处理。注意：这里的静态资源是放置在web根目录下，而非WEB-INF 下。
　　可能这里的描述有点不好懂（我自己也这么觉得），所以简单举个例子，例如：在webroot目录下有一个图片：1.png 我们知道Servelt规范中web根目录（webroot）下的文件可以直接访问的，但是由于DispatcherServlet配置了映射路径是：/ ，它几乎把所有的请求都拦截了，从而导致1.png 访问不到，这时注册一个DefaultServletHttpRequestHandler 就可以解决这个问题。其实可以理解为DispatcherServlet破坏了[Servlet](https://so.csdn.net/so/search?q=Servlet&spm=1001.2101.3001.7020)的一个特性（根目录下的文件可以直接访问），DefaultServletHttpRequestHandler是帮助回归这个特性的。

### 3.5 configureViewResolvers：视图解析器

这个方法是用来配置视图解析器的，该方法的参数ViewResolverRegistry 是一个注册器，用来注册你想自定义的视图解析器等。ViewResolverRegistry 常用的几个方法：https://blog.csdn.net/fmwind/article/details/81235401

```java
/**
 * 配置请求视图映射
 * @return
 */
@Bean
public InternalResourceViewResolver resourceViewResolver(){
	InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
	//请求视图文件的前缀地址
	internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
	//请求视图文件的后缀
	internalResourceViewResolver.setSuffix(".jsp");
	return internalResourceViewResolver;
}

/**
 * 视图配置
 * @param registry
 */
@Override
public void configureViewResolvers(ViewResolverRegistry registry) {
	super.configureViewResolvers(registry);
	registry.viewResolver(resourceViewResolver());
	/*registry.jsp("/WEB-INF/jsp/",".jsp");*/
}
```

### 3.6 configureContentNegotiation：配置内容裁决的一些参数

### 3.7 addCorsMappings：跨域

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    super.addCorsMappings(registry);
    registry.addMapping("/cors/**")
            .allowedHeaders("*")
            .allowedMethods("POST","GET")
            .allowedOrigins("*");
}
```

### 3.8 configureMessageConverters：信息转换器

```xjava
/**
 * 消息内容转换配置
 * 配置fastJson返回json转换
 * @param converters
 */

@Override
public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //调用父类的配置
    super.configureMessageConverters(converters);
    //创建fastJson消息转换器
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    //创建配置类
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    //修改配置返回内容的过滤
    fastJsonConfig.setSerializerFeatures(
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullStringAsEmpty
    );
    fastConverter.setFastJsonConfig(fastJsonConfig);
    //将fastjson添加到视图消息转换器列表内
    converters.add(fastConverter);
}
```