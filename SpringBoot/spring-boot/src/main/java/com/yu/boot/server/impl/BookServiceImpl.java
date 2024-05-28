package com.yu.boot.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.boot.entity.Book;
import com.yu.boot.mapper.BookMapper;
import com.yu.boot.server.BookService;
import org.springframework.stereotype.Service;

/**
 * @author Yue
 */
@Service("bookService")
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}
