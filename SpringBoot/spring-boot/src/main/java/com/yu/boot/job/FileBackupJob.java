package com.yu.boot.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yu.boot.entity.FileCustom;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileBackupJob implements SimpleJob {
    /**
     * 每次任务执行要备份文件的数量
     */
    private final int FETCH_SIZE = 1;
    /**
     * 文件列表（模拟）
     */
    public static List<FileCustom> files = new ArrayList<>();
    /**
     * 任务调度执行方法
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {
//作业分片信息
        int shardingItem = shardingContext.getShardingItem();
        System.out.println(String.format("作业分片：%d",shardingItem));
//获取未备份的文件
        List<FileCustom> fileCustoms = fetchUnBackupFiles(FETCH_SIZE);
//文件备份
        backupFiles(fileCustoms);
    }
    /**
     * 获取未备份的文件
     * @param count
     * @return
     */
    public List<FileCustom> fetchUnBackupFiles(int count){
        List<FileCustom> fetchList = new ArrayList<>();
        int num = 0;
        for(FileCustom fileCustom:files){
            if(num>=count){
                break;
            }
//未备份的文件则放入列表
            if(!fileCustom.getBackedUp()){
                fetchList.add(fileCustom);
                num++;
            }
        }
//ManagementFactory.getRuntimeMXBean()获取当前JVM进程的PID
        System.out.println(String.format("%sTime:%s,已获取%d文件",
                ManagementFactory.getRuntimeMXBean().getName(),new SimpleDateFormat("hh:mm:ss").format(new
                        Date()),num));
        return fetchList;
    }
    /**
     * 备份文件
     * @param files
     */
    public void backupFiles(List<FileCustom> files){
        for(FileCustom file : files){
            //标记文件数据为已备份
            file.setBackedUp(Boolean.TRUE);
            System.out.println(String.format("已备份文件:%s 文件类型:%s",file.getName(),file.getType()));
        }
    }
}
