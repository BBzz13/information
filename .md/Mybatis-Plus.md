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

## 一、添加依赖

​    MyBatis-Plus 从 3.0.3 之后移除了代码生成器与模板引擎的默认依赖，需要手动添加相关依赖。以下是AutoGenerator代码生成器和freemarker模板引擎依赖（模板引擎选一种，也可以自定义模板引擎）：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<!--mybatis-plus（springboot版）-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.0</version>
</dependency>
<!--mybatis-plus代码生成器-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.4.0</version>
</dependency>
<!--Velocity（默认）模板引擎-->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.2</version>
</dependency>
<!--freemarker模板引擎（博主用的）-->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.30</version>
</dependency>
<!--beetl模板引擎-->
<dependency>
    <groupId>com.ibeetl</groupId>
    <artifactId>beetl</artifactId>
    <version>3.2.1.RELEASE</version>
</dependency>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

## 二、自定义参数

### 1、配置 GlobalConfig（全局配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 全局配置
GlobalConfig gc = new GlobalConfig();
//项目根目录
String projectPath = System.getProperty("user.dir");
//用于多个模块下生成到精确的目录下（我设置在桌面）
//String projectPath = "C:/Users/xie/Desktop";
//代码生成目录
gc.setOutputDir(projectPath + "/src/main/java");
//开发人员
gc.setAuthor("先谢郭嘉");
// 是否打开输出目录(默认值：null)
gc.setOpen(false);
//实体属性 Swagger2 注解
gc.setSwagger2(true);
//去掉接口上的I
//gc.setServiceName("%Service");
// 配置时间类型策略（date类型），如果不配置会生成LocalDate类型
gc.setDateType(DateType.ONLY_DATE);
// 是否覆盖已有文件(默认值：false)
gc.setFileOverride(true);
//把全局配置添加到代码生成器主类
mpg.setGlobalConfig(gc);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 2、 配置 DataSourceConfig（数据源配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 数据源配置
DataSourceConfig dsc = new DataSourceConfig();
//数据库连接
dsc.setUrl("jdbc:mysql://localhost:3306/blog?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
// 数据库 schema name
//dsc.setSchemaName("public");
// 数据库类型
dsc.setDbType(DbType.MYSQL);
// 驱动名称
dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//用户名
dsc.setUsername("root");
//密码
dsc.setPassword("430423");
//把数据源配置添加到代码生成器主类
mpg.setDataSource(dsc);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 3、 配置PackageConfig（包名配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 包配置
PackageConfig pc = new PackageConfig();
// 添加这个后 会以一个实体为一个模块 比如user实体会生成user模块 每个模块下都会生成三层
// pc.setModuleName(scanner("模块名"));
// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
pc.setParent("com.xxgg.blog");
// Service包名
pc.setService("service");
// Entity包名
pc.setEntity("entity");
// ServiceImpl包名
pc.setServiceImpl("service.impl");
// Mapper包名
pc.setMapper("mapper");
// Controller包名
pc.setController("controller");
// Mapper.xml包名
pc.setXml("mapper");
// 把包配置添加到代码生成器主类
mpg.setPackageInfo(pc);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

4、 配置InjectionConfig（自定义配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 自定义配置
InjectionConfig cfg = new InjectionConfig() {
    @Override
    public void initMap() {
        // to do nothing
    }
};

// 如果模板引擎是 freemarker
String templatePath = "/templates/mapper.xml.ftl";
// 如果模板引擎是 velocity
// String templatePath = "/templates/mapper.xml.vm";

// 自定义输出配置
List<FileOutConfig> focList = new ArrayList<>();
// 自定义配置会被优先输出
focList.add(new FileOutConfig(templatePath) {
    @Override
    public String outputFile(TableInfo tableInfo) {
    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
    }
});
/*
cfg.setFileCreate(new IFileCreate() {
    @Override
    public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
        // 判断自定义文件夹是否需要创建
        checkDir("调用默认方法创建的目录，自定义目录用");
        if (fileType == FileType.MAPPER) {
            // 已经生成 mapper 文件判断存在，不想重新生成返回 false
            return !new File(filePath).exists();
        }
        // 允许生成模板文件
        return true;
       }
});
*/
cfg.setFileOutConfigList(focList);
mpg.setCfg(cfg);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 5、 配置TemplateConfig（模板配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 配置模板
TemplateConfig templateConfig = new TemplateConfig();

// 配置自定义输出模板
//指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
// templateConfig.setEntity("templates/entity2.java");
// templateConfig.setService();
// templateConfig.setController();

templateConfig.setXml(null);
mpg.setTemplate(templateConfig);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 6、配置StrategyConfig（策略配置）

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
// 策略配置，我喜欢叫数据库表配置
StrategyConfig strategy = new StrategyConfig();
// 数据库表映射到实体的命名策略:下划线转驼峰
strategy.setNaming(NamingStrategy.underline_to_camel);
// 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
strategy.setColumnNaming(NamingStrategy.underline_to_camel);
// 实体是否为lombok模型（默认 false）
strategy.setEntityLombokModel(true);
// 生成 @RestController 控制器
strategy.setRestControllerStyle(true);
// 实体类主键名称设置
strategy.setSuperEntityColumns("id");
// 需要包含的表名，允许正则表达式
//这里我做了输入设置
strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
// 需要排除的表名，允许正则表达式
//strategy.setExclude("***");
// 是否生成实体时，生成字段注解 默认false;
strategy.setEntityTableFieldAnnotationEnable(true);
// 驼峰转连字符
strategy.setControllerMappingHyphenStyle(true);
// 表前缀
strategy.setTablePrefix(pc.getModuleName() + "_");
// 把数据库配置添加到代码生成器主类
mpg.setStrategy(strategy);
```



### 7、生成

```
// 在代码生成器主类上配置模板引擎
mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//生成
mpg.execute();
```

## 三、演示

### 1、代码



```
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw  new MybatisPlusException("请输入正确的" + tip + "！");
    }
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        //作者
        gc.setAuthor("yxl");
        //打开输出目录
        gc.setOpen(false);
        //xml开启 BaseResultMap
        gc.setBaseResultMap(true);
        //xml 开启BaseColumnList
        gc.setBaseColumnList(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3310/his-base-center?"+
                "useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia" +
                "/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.his.base.org")
                .setEntity("pojo")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/hx-com.his.base.api/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper"
                        + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.no_change);
        //lombok模型
        strategy.setEntityLombokModel(true);
        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //表前缀
        strategy.setTablePrefix(pc.getModuleName()+"_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 2、pom.xml文件

### 

```
<!--web 依赖-->



<dependency>

<groupId>org.springframework.boot</groupId>

<artifactId>spring-boot-starter-web</artifactId>

</dependency>

<!--mybatis-plus 依赖-->



<dependency>

<groupId>com.baomidou</groupId>

<artifactId>mybatis-plus-boot-starter</artifactId>

<version>3.3.1.tmp</version>

</dependency>

<!--mybatis-plus 代码生成器依赖-->



<dependency>

<groupId>com.baomidou</groupId>

<artifactId>mybatis-plus-generator</artifactId>

<version>3.3.1.tmp</version>

</dependency>

<!--freemarker 依赖-->



<dependency>

<groupId>org.freemarker</groupId>

<artifactId>freemarker</artifactId>

</dependency>

<!--mysql 依赖-->



<dependency>

<groupId>mysql</groupId>

<artifactId>mysql-connector-java</artifactId>

<scope>runtime</scope>

</dependency>
```

