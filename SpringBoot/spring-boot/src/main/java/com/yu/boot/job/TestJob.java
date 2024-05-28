package com.yu.boot.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.debug("------Thread ID: {}, 任务总片数: {}, 当前分片项: {}, 分片参数：{}", Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(), shardingContext.getShardingItem(), shardingContext.getShardingParameter());
        System.out.println("abcdefg");
    }
}
