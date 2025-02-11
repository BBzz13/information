package com.yu.common.job.init;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/**
 * 任务初始化基类
 */
public abstract class AbstractJobInitialization implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化任务
     *
     * @param jobName       任务名称
     * @param jobType       任务类型
     * @param configuration 配置
     */
    protected void initJob(String jobName, JobType jobType, ElasticJobProperties.JobConfiguration configuration) {
        //向spring容器中注册作业任务
        ElasticJob elasticJob = registerElasticJob(jobName, configuration.getJobClass(), jobType);
        //获取注册中心
        ZookeeperRegistryCenter regCenter = getZookeeperRegistryCenter(configuration.getRegistryCenterRef());
        //构建核心配置
        JobCoreConfiguration jobCoreConfiguration = getJobCoreConfiguration(jobName, configuration);
        //获取作业类型配置
        JobTypeConfiguration jobTypeConfiguration = getJobTypeConfiguration(jobName, jobCoreConfiguration);
        //获取Lite作业配置
        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobTypeConfiguration, configuration);
        //获取作业事件追踪的数据源配置
        JobEventRdbConfiguration jobEventRdbConfiguration = getJobEventRdbConfiguration(configuration.getEventTraceRdbDataSource());
        //获取作业监听器
        ElasticJobListener[] elasticJobListeners = creatElasticJobListeners(configuration.getListener());
        elasticJobListeners = null == elasticJobListeners ? new ElasticJobListener[0] : elasticJobListeners;
        //注册作业
        if (null == jobEventRdbConfiguration) {
            new SpringJobScheduler(elasticJob, regCenter, liteJobConfiguration, elasticJobListeners).init();
        } else {
            new SpringJobScheduler(elasticJob, regCenter, liteJobConfiguration, jobEventRdbConfiguration, elasticJobListeners).init();
        }
    }

    /**
     * 获取作业类型配置
     *
     * @param jobName              任务名称
     * @param jobCoreConfiguration 任务核心配置
     * @return JobTypeConfiguration
     */
    public abstract JobTypeConfiguration getJobTypeConfiguration(String jobName, JobCoreConfiguration jobCoreConfiguration);

    /**
     * 获取作业任务实例
     *
     * @param jobName  任务名称
     * @param jobType  任务类型
     * @param strClass 任务类全路径
     * @return ElasticJob
     */
    private ElasticJob registerElasticJob(String jobName, String strClass, JobType jobType) {
        switch (jobType) {
            case SIMPLE:
                return registerBean(jobName, strClass, SimpleJob.class);
            case DATAFLOW:
                return registerBean(jobName, strClass, DataflowJob.class);
            default:
                return null;
        }
    }

    /**
     * 获取注册中心
     *
     * @param registryCenterRef 注册中心引用
     * @return ZookeeperRegistryCenter
     */
    private ZookeeperRegistryCenter getZookeeperRegistryCenter(String registryCenterRef) {
        if (StringUtils.isBlank(registryCenterRef)) {
            registryCenterRef = DEFAULT_REGISTRY_CENTER_NAME;
        }
        if (!applicationContext.containsBean(registryCenterRef)) {
            throw new RuntimeException("not exist ZookeeperRegistryCenter [" + registryCenterRef + "] !");
        }
        return applicationContext.getBean(registryCenterRef, ZookeeperRegistryCenter.class);
    }

    /**
     * 获取作业事件追踪的数据源配置
     *
     * @param eventTraceRdbDataSource 作业事件追踪的数据源Bean引用
     * @return JobEventRdbConfiguration
     */
    private JobEventRdbConfiguration getJobEventRdbConfiguration(String eventTraceRdbDataSource) {
        if (StringUtils.isBlank(eventTraceRdbDataSource)) {
            return null;
        }
        if (!applicationContext.containsBean(eventTraceRdbDataSource)) {
            throw new RuntimeException("not exist datasource [" + eventTraceRdbDataSource + "] !");
        }
        DataSource dataSource = (DataSource) applicationContext.getBean(eventTraceRdbDataSource);
        return new JobEventRdbConfiguration(dataSource);
    }


    /**
     * 构建Lite作业
     *
     * @param jobTypeConfiguration 任务类型
     * @param jobConfiguration     任务配置
     * @return LiteJobConfiguration
     */
    private LiteJobConfiguration getLiteJobConfiguration(JobTypeConfiguration jobTypeConfiguration, ElasticJobProperties.JobConfiguration jobConfiguration) {
        //构建Lite作业
        return LiteJobConfiguration.newBuilder(Objects.requireNonNull(jobTypeConfiguration))
                .monitorExecution(jobConfiguration.isMonitorExecution())
                .monitorPort(jobConfiguration.getMonitorPort())
                .maxTimeDiffSeconds(jobConfiguration.getMaxTimeDiffSeconds())
                .jobShardingStrategyClass(jobConfiguration.getJobShardingStrategyClass())
                .reconcileIntervalMinutes(jobConfiguration.getReconcileIntervalMinutes())
                .disabled(jobConfiguration.isDisabled())
                .overwrite(jobConfiguration.isOverwrite()).build();
    }

    /**
     * 构建任务核心配置
     *
     * @param jobName          任务执行名称
     * @param jobConfiguration 任务配置
     * @return JobCoreConfiguration
     */
    protected JobCoreConfiguration getJobCoreConfiguration(String jobName, ElasticJobProperties.JobConfiguration jobConfiguration) {
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobName, jobConfiguration.getCron(), jobConfiguration.getShardingTotalCount())
                .shardingItemParameters(jobConfiguration.getShardingItemParameters())
                .jobParameter(jobConfiguration.getJobParameter())
                .failover(jobConfiguration.isFailover())
                .misfire(jobConfiguration.isMisfire())
                .description(jobConfiguration.getDescription());
        if (StringUtils.isNotBlank(jobConfiguration.getJobExceptionHandler())) {
            builder.jobProperties(JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobConfiguration.getJobExceptionHandler());
        }
        if (StringUtils.isNotBlank(jobConfiguration.getExecutorServiceHandler())) {
            builder.jobProperties(JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), jobConfiguration.getExecutorServiceHandler());
        }
        return builder.build();
    }

    /**
     * 获取监听器
     *
     * @param listener 监听器配置
     * @return ElasticJobListener[]
     */
    private ElasticJobListener[] creatElasticJobListeners(ElasticJobProperties.JobConfiguration.Listener listener) {

        if (null == listener) {
            return null;
        }

        List<ElasticJobListener> elasticJobListeners = new ArrayList<>(2);

        //注册每台作业节点均执行的监听
        ElasticJobListener elasticJobListener = registerBean(listener.getListenerClass(), listener.getListenerClass(), ElasticJobListener.class);
        if (null != elasticJobListener) {
            elasticJobListeners.add(elasticJobListener);
        }

        //注册分布式监听者
        AbstractDistributeOnceElasticJobListener distributedListener = registerBean(listener.getDistributedListenerClass(), listener.getDistributedListenerClass(),
                AbstractDistributeOnceElasticJobListener.class, listener.getStartedTimeoutMilliseconds(), listener.getCompletedTimeoutMilliseconds());
        if (null != distributedListener) {
            elasticJobListeners.add(distributedListener);
        }

        if (CollectionUtils.isEmpty(elasticJobListeners)) {
            return null;
        }

        //集合转数组
        ElasticJobListener[] elasticJobListenerArray = new ElasticJobListener[elasticJobListeners.size()];
        for (int i = 0; i < elasticJobListeners.size(); i++) {
            elasticJobListenerArray[i] = elasticJobListeners.get(i);
        }
        return elasticJobListenerArray;
    }

    /**
     * 向spring容器中注册bean
     *
     * @param beanName            bean名字
     * @param strClass            类全路径
     * @param tClass              类类型
     * @param constructorArgValue 构造函数参数
     * @param <T>                 泛型
     * @return T
     */
    protected <T> T registerBean(String beanName, String strClass, Class<T> tClass, Object... constructorArgValue) {

        //判断是否配置了监听者
        if (StringUtils.isBlank(strClass)) {
            return null;
        }

        if (StringUtils.isBlank(beanName)) {
            beanName = strClass;
        }

        //判断监听者是否已经在spring容器中存在
        if (applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName, tClass);
        }

        //不存在则创建并注册到Spring容器中
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(strClass);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        //设置参数
        for (Object argValue : constructorArgValue) {
            beanDefinitionBuilder.addConstructorArgValue(argValue);
        }
        getDefaultListableBeanFactory().registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return applicationContext.getBean(beanName, tClass);
    }

    /**
     * 获取beanFactory
     *
     * @return DefaultListableBeanFactory
     */
    private DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
    }
}