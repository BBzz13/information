package com.yu.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.boot.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yue
 */
@Mapper
public interface BookMapper extends BaseMapper<Book>{
}