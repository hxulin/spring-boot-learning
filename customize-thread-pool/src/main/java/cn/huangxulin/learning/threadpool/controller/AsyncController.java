package cn.huangxulin.learning.threadpool.controller;

import cn.huangxulin.learning.threadpool.service.AsyncService;
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
     * 测试自定义线程池触发告警
     *
     * @return ignored
     */
    @GetMapping("/async/test1")
    @SneakyThrows
    public Object test1() {
        asyncService.testWarning();
        return "testWarning";
    }
}
