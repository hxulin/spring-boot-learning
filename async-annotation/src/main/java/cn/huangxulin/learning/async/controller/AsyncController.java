package cn.huangxulin.learning.async.controller;

import cn.huangxulin.learning.async.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 使用异步，等待返回结果（5秒多）
     *
     * @return ignored
     */
    @GetMapping("/async/test2")
    @SneakyThrows
    public Object test2() {
        long start = System.currentTimeMillis();
        String result = asyncService.testAsync().get();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;
    }
}
