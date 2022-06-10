package cn.huangxulin.learning.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author hxl
 */
@EnableAsync
@SpringBootApplication
public class AsyncAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncAnnotationApplication.class, args);
    }

}
