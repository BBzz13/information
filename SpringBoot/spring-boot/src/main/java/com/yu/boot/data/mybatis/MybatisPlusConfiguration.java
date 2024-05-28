//package com.yu.boot.data.mybatis;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
////import com.yu.boot.data.config.MybatisProperties;
//import com.yu.boot.data.druid.DruidSqlLogFilter;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.sql.DataSource;
//
///**
// * @author liuyu
// * @date 2022年11月9日15:07:15
// * # @Configuration //配置类
// * # @ConditionalOnBean(DataSource.class) //当上下文存在DataSource 时才会实例化一个bean
// * # @AutoConfigureAfter(DataSourceAutoConfiguration.class) //加载类之后架子当前类
// * # @EnableConfigurationProperties(MybatisProperties.class) //让使用了 @ConfigurationProperties 注解的类生效,并且将该类注入到 IOC 容器中,交由 IOC 容器进行管理
// */
//@Configuration
//@ConditionalOnBean(DataSource.class)
//@AutoConfigureAfter(DataSourceAutoConfiguration.class)
//@EnableConfigurationProperties(MybatisProperties.class)
//public class MybatisPlusConfiguration implements WebMvcConfigurer {
//    /**
//     * SQL 日志格式化
//     * @return DruidSqlLogFilter
//     */
//    @Bean
//    public DruidSqlLogFilter sqlLogFilter() {
//        return new DruidSqlLogFilter();
//    }
//
//    /**
//     * 审计字段自动填充
//     * @return {@link MetaObjectHandler}
//     */
////    @Bean
////    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
////        return new MybatisPlusMetaObjectHandler();
////    }
//}
