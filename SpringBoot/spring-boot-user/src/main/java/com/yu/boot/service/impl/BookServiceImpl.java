package com.yu.boot.service.impl;

import com.yu.boot.entity.BookEntity;
import com.yu.boot.mapper.BookMapper;
import com.yu.boot.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuyu
 * @since 2023-01-12
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, BookEntity> implements IBookService {

}
