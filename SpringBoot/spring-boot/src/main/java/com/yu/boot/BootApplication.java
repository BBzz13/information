package com.yu.boot;

//import com.yu.boot.stream.Source;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author ly
 * @date 2022年11月7日 17点59分
 */

@SpringBootApplication
//@EnableBinding(Source.class)
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class);
    }
}
