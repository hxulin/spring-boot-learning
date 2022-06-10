package cn.huangxulin.learning.threadpool.controller;

import cn.huangxulin.learning.threadpool.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hxl
 */
@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    /**
     * 使用异步任务，不等待返回结果（几毫秒）
     *
     * @return ignored
     */
    @GetMapping("/async/test1")
    public Object test1() {
        long start = System.currentTimeMillis();
        asyncService.testAsync();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return "test1";
    }

    @GetMapping("/async/test2")
    @SneakyThrows
    public Object test2(@RequestParam Integer number) {
        return asyncService.testException(number).get();
    }

    @GetMapping("/async/test3")
    public Object test3(@RequestParam Integer number) {
        asyncService.testVoidException(number);
        return "test3";
    }
}
