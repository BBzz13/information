package com.yu.boot.controller;

import com.yu.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyu
 * @date 2022年11月7日 18点00分
 */
@RestController
@RequestMapping("/test")
public class TestController {

//    @Autowired
//    @Qualifier("output")
//    MessageChannel output;

//    @GetMapping("/test")
//    public R discover(){
//        output.send(MessageBuilder.withPayload("Hello world").build());
//        return  R.ok("请求成功");
//    }
}
