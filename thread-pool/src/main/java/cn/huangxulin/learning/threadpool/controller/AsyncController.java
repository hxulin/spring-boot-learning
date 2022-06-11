package cn.huangxulin.learning.threadpool.controller;

import cn.huangxulin.learning.threadpool.config.ThreadPoolConfig;
import cn.huangxulin.learning.threadpool.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * @author hxl
 */
@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("/async/test1")
    public Object test1() {
        long start = System.currentTimeMillis();
        asyncService.testAsync();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return "test1";
    }

    /**
     * 测试异常处理，当异步任务返回 {@link Future} 时，
     * 不会触发 {@link ThreadPoolConfig#getAsyncUncaughtExceptionHandler()} 异常处理
     *
     * @return ignored
     */
    @GetMapping("/async/test2")
    @SneakyThrows
    public Object test2(@RequestParam Integer number) {
        return asyncService.testException(number).get();
    }

    /**
     * 测试异常处理，当异步任务返回 void 时，
     * 会触发 {@link ThreadPoolConfig#getAsyncUncaughtExceptionHandler()} 异常处理
     *
     * @return ignored
     */
    @GetMapping("/async/test3")
    public Object test3(@RequestParam Integer number) {
        asyncService.testVoidException(number);
        return "test3";
    }
}
