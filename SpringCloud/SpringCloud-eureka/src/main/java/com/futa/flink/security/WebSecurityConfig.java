package com.futa.flink.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Yue
 * 1.用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，
 *   这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，
 *   并用于构建bean定义，初始化Spring容器。
 * 2.加载了WebSecurityConfiguration配置类, 配置安全认证策略。 加载了AuthenticationConfiguration, 配置了认证信息。
 * 3.开启spring方法级安全
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Bean
    protected SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf()
                .disable()
                .authorizeRequests()
                // url匹配/actuator/**的资源，经过认证后才能访问。 所有请求必须认证才能访问，
                //.antMatchers("/actuator/**")
                //.permitAll()
                //其他url完全开放。
                .anyRequest()
                //所有请求必须认证才能访问，
                .authenticated()
                .and()
                .httpBasic();
//        .regexMatchers() 正则匹配。
//        参数一：可以放行指定的请求方法(只写一个参数的话，代表任何的请求方法都可以)。。
//        参数二：api接口，或一个正则表达式。
//        例：只放行get请求方法的 /demo 接口
//                .regexMatchers(HttpMethod.GET, "/demo").permitAll()

//        .mvcMatchers() MVC匹配。
//        只要是 /test/demo的api就放行，下边的servletPath() 与下边配置文件中的servlet.path 对应，
//        如果加上此配置，访问所有的api都需要加上前缀 /test
//                .mvcMatchers("/demo").servletPath("/test").permitAll()
        return httpSecurity.build();
    }
}
