package cn.huangxulin.learning.threadpool.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author hxl
 */
@Service
public class AsyncService {

    @Async
    @SneakyThrows
    public void testAsync() {
        System.out.println(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(5);
        System.out.println("async task success.");
    }

    @Async
    public Future<Integer> testException(int number) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(10 / number);
        return new AsyncResult<>(10 / number);
    }

    @Async
    public void testVoidException(int number) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(10 / number);
    }
}
