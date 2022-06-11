package cn.huangxulin.learning.threadpool.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author hxl
 */
@Service
public class AsyncService {

    @Async
    @SneakyThrows
    public void testWarning() {
        long maxValue = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (maxValue > Integer.MAX_VALUE) {
                break;
            }
            Random random = new Random();
            maxValue = maxValue + i - random.nextInt(Integer.MAX_VALUE / 2);
        }
        System.out.println(Thread.currentThread().getName() + " : " + maxValue);
    }
}
