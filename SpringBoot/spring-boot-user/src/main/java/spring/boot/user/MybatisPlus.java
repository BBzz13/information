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
