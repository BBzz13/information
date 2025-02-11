# Mybatis-Plus常用配置

```yml
mybatis-plus:
  mapperPackage: com.**.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: com.**.**.domain
  # 针对 typeAliasesPackage，如果配置了该属性，则仅仅会扫描路径下以该类作为父类的域对象
  #typeAliasesSuperType: Class<?>
  # 如果配置了该属性，SqlSessionFactoryBean 会把该包下面的类注册为对应的 TypeHandler
  #typeHandlersPackage: null
  # 如果配置了该属性，会将路径下的枚举类进行注入，让实体类字段能够简单快捷的使用枚举属性
  #typeEnumsPackage: null
  # 启动时是否检查 MyBatis XML 文件的存在，默认不检查
  checkConfigLocation: false
  # 通过该属性可指定 MyBatis 的执行器，MyBatis 的执行器总共有三种：
  # SIMPLE：该执行器类型不做特殊的事情，为每个语句的执行创建一个新的预处理语句（PreparedStatement）
  # REUSE：该执行器类型会复用预处理语句（PreparedStatement）
  # BATCH：该执行器类型会批量执行所有的更新语句
  executorType: SIMPLE
  # 指定外部化 MyBatis Properties 配置，通过该配置可以抽离配置，实现不同环境的配置部署
  configurationProperties: null
  configuration:
     # 指定外部化 MyBatis Properties 配置，通过该配置可以抽离配置，实现不同环境的配置部署
    jdbc-type-for-null: 'null'
    #在一般查询中，如果用map接受查询结果时，会自动将查询结果为null的字段忽略，这样就造成取参数时报空指针异常的情况。
    #如果设置了这条属性之后，mybatis就不会忽略这些字段，你依然能get到这些key，只不过value为null，这样也方便。
    #1.如果整个查询的所有字段都没有值，就是查询到0条记录时，那么接受到的map是什么结果？
    #答案是，我们会接收到一个所有key值都为null 的map 而不是一个为null的map。
    #2.如果只查询一个字段，而用map接收，此时为null是什么结果？
    #答案是，我们会接收到一个为null的map，跟上面情况正好相反
    call-setters-on-nulls: true
    # 自动驼峰命名规则（camel case）映射
    # 如果您的数据库命名符合规则无需使用 @TableField 注解指定数据库字段名
    mapUnderscoreToCamelCase: true
    # 默认枚举处理类,如果配置了该属性,枚举将统一使用指定处理器进行处理
    # org.apache.ibatis.type.EnumTypeHandler : 存储枚举的名称
    # org.apache.ibatis.type.EnumOrdinalTypeHandler : 存储枚举的索引
    # com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler : 枚举类需要实现IEnum接口或字段标记@EnumValue注解.
    defaultEnumTypeHandler: org.apache.ibatis.type.EnumTypeHandler
    # 当设置为 true 的时候，懒加载的对象可能被任何懒属性全部加载，否则，每个属性都按需加载。需要和 lazyLoadingEnabled 一起使用。
    aggressiveLazyLoading: true
    # MyBatis 自动映射策略
    # NONE：不启用自动映射
    # PARTIAL：只对非嵌套的 resultMap 进行自动映射
    # FULL：对所有的 resultMap 都进行自动映射
    autoMappingBehavior: PARTIAL
    # MyBatis 自动映射时未知列或未知属性处理策
    # NONE：不做任何处理 (默认值)
    # WARNING：以日志的形式打印相关警告信息
    # FAILING：当作映射失败处理，并抛出异常和详细信息
    autoMappingUnknownColumnBehavior: NONE
    # Mybatis一级缓存，默认为 SESSION
    # SESSION session级别缓存，同一个session相同查询语句不会再次查询数据库
    # STATEMENT 关闭一级缓存
    localCacheScope: SESSION
    # 开启Mybatis二级缓存，默认为 true
    cacheEnabled: true
  global-config:
    # 是否打印 Logo banner
    banner: true
    # 是否初始化 SqlRunner
    enableSqlRunner: false
    dbConfig:
      # 主键类型
      # AUTO 数据库ID自增
      # NONE 空
      # INPUT 用户输入ID
      # ASSIGN_ID 全局唯一ID
      # ASSIGN_UUID 全局唯一ID UUID
      idType: AUTO
      # 表名前缀
      tablePrefix: null
      # 字段 format,例: %s,(对主键无效)
      columnFormat: null
      # 表名是否使用驼峰转下划线命名,只对表名生效
      tableUnderline: true
      # 大写命名,对表名和字段名均生效
      capitalMode: false
      # 全局的entity的逻辑删除字段属性名
      logicDeleteField: deleteFlag
      # 逻辑已删除值
      logicDeleteValue: 1
      # 逻辑未删除值
      logicNotDeleteValue: 0
      # 字段验证策略之 insert,在 insert 的时候的字段验证策略
      # IGNORED 忽略判断
      # NOT_NULL 非NULL判断
      # NOT_EMPTY 非空判断(只对字符串类型字段,其他类型字段依然为非NULL判断)
      # DEFAULT 默认的,一般只用于注解里
      # NEVER 不加入 SQL
      insertStrategy: NOT_EMPTY
      # 字段验证策略之 update,在 update 的时候的字段验证策略
      updateStrategy: NOT_NULL
      # 字段验证策略之 select,在 select 的时候的字段验证策略既 wrapper 根据内部 entity 生成的 where 条件
      selectStrategy: NOT_EMPTY

```

# Mybatis-Plus代码生成器

```java
package spring.boot.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;

public class MybatisPlus {
    public static void main(String[] args) {
        // 代码生成器
        //    1.创建代码生成器对象
        //    2.执行代码生成器
        // mp包里面的AutoGenerator
        AutoGenerator autoGenerator = new AutoGenerator();
        // 数据源配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setUrl("jdbc:mysql://localhost:3306/ssm_db?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        autoGenerator.setDataSource(dataSource);


        //会在D盘生成一个com文件，但是这个位置是不对的，需要我们再进一步配置
        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //默认输出D盘根下，设置到这一目录下  C:/yu/代码/Frame/spring-boot-user/src/main/java
        globalConfig.setOutputDir("C:/yu/代码/Frame/spring-boot-user/src/main/java");
        //设置完之后是否打开资源管理器   NO
        globalConfig.setOpen(false);
        //设置作者
        globalConfig.setAuthor("liuyu");
        //设置是否覆盖原始生成的文件
        globalConfig.setFileOverride(true);
        //设置数据层接口名，%s为占位符  代表数据库中的表名或模块名
        globalConfig.setMapperName("%sMapper");
        //设置id生成策略
        globalConfig.setIdType(IdType.ASSIGN_ID);
        //设置Entity层名称
        globalConfig.setEntityName("%sEntity");

        autoGenerator.setGlobalConfig(globalConfig);

        //包相关配置
        PackageConfig packageConfig = new PackageConfig();
        //设置生成的包名，与代码所在位置不冲突，二者叠加组成完整路径
        packageConfig.setParent("com.yu.boot");
        //设置实体类包名
        packageConfig.setEntity("entity");
        //设置数据层包名
        packageConfig.setMapper("mapper");
        autoGenerator.setPackageInfo(packageConfig);


        //第四步：   策略设置
        StrategyConfig strategyConfig = new StrategyConfig();
        //设置当前参与生成的表名，参数为可变参数   生成指定表
        //strategyConfig.setInclude("tbl_user");
        //设置数据库表的前缀名称，模块名=数据库表名-前缀名   User=tbl_user - tbl_
        //strategyConfig.setTablePrefix("tbl_");
        //是否启用Rest风格
        strategyConfig.setRestControllerStyle(true);
        //设置乐观锁字段名
        //strategyConfig.setVersionFieldName("version");
        //设置逻辑删除字段名
        //strategyConfig.setLogicDeleteFieldName("deleted");
        //设置是否启用Lombok
        strategyConfig.setEntityLombokModel(true);
        autoGenerator.setStrategy(strategyConfig);
        //执行
        autoGenerator.execute();
    }
}

```

