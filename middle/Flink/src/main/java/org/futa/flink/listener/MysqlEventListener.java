package org.futa.flink.listener;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Dataempower6
 */
@Component
public class MysqlEventListener implements ApplicationRunner {

    /**
     * CDC数据源配置
     */
    @Value("${cdc.datasource.host}")
    private String host;
    @Value("${cdc.datasource.port:3306}")
    private Integer port;
    @Value("${cdc.datasource.database}")
    private String database;
    @Value("${cdc.datasource.tableList}")
    private String tableList;
    @Value("${cdc.datasource.username}")
    private String username;
    @Value("${cdc.datasource.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MySqlSource<DataChangeInfo> mySqlSource = MySqlSource.<DataChangeInfo>builder()
                .hostname(host)
                .port(port)
                // 设置捕获的数据库， 如果需要同步整个数据库，请将 tableList 设置为 ".*".
                .databaseList(database)
                // 设置捕获的表，数据库.表名
                .tableList(tableList)
                .username(username)
                .password(password)
                // 将 SourceRecord 转换为 自定义对象
                .deserializer(new MysqlDeserialization())
                /**initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(StartupOptions.latest())
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Configuration config = new Configuration();
        // 设置增量快照开启为 true
        config.setBoolean("scan.incremental.snapshot.enabled", true);
        env.configure(config);
        env.setParallelism(1);
        DataStreamSource<DataChangeInfo> streamSource = env
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .setParallelism(1);
        streamSource.addSink(new DataChangeSink());


//        MySqlSource<DataChangeInfo> mySqlSource2 = MySqlSource.<DataChangeInfo>builder()
//                .hostname(host)
//                .port(port)
//                .databaseList(database2)
//                .tableList(tableList2)
//                .username(username)
//                .password(password)
//                .deserializer(new MysqlDeserialization())
//                .startupOptions(StartupOptions.initial())
//                .build();
//        DataStreamSource<DataChangeInfo> streamSource2 = env
//                .fromSource(mySqlSource2, WatermarkStrategy.noWatermarks(), "MySQL Source2")
//                .setParallelism(1);
//        streamSource2.addSink(new DataChangeSinkMysql());
//        env.execute("Sending change data from different databases to different sinks");

        env.execute("mysql-stream-cdc");
    }

}

