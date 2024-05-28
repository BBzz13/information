package com.yu.common.test.annotation;

import com.yu.common.test.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({TestConfiguration.class})
public @interface EnableTest {
}
