package com.yu.boot.controller;

import com.yu.boot.entity.Book;
import com.yu.boot.exception.CustomException;
import com.yu.boot.server.impl.BookServiceImpl;
import com.yu.common.util.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liuyu
 */
@RestController
@AllArgsConstructor
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookServiceImpl bookService;

    @GetMapping("/{id}")
    public R<Book> getById(@PathVariable("id") Integer id)  {
        Book byId = bookService.getById(id);
        if (byId == null) {
            throw new CustomException("不存在此书籍");
        }
        return R.ok(byId);
    }

    @PostMapping("/save")
    public R<Boolean> saveBook(@RequestBody Book book){
        boolean save = bookService.save(book);
        if (!save){
            throw new CustomException("保存失败");
        }
        return R.ok(true);
    }

}
