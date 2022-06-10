package cn.huangxulin.learning.async.service;

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
    public Future<String> testAsync() {
        TimeUnit.SECONDS.sleep(5);
        return new AsyncResult<>("hello");
    }
}
