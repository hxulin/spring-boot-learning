package cn.huangxulin.learning.threadpool.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author hxl
 */
@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfig extends AsyncConfigurerSupport {

    private final ThreadPoolProperties properties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new CustomizeThreadPoolTaskExecutor(properties);
        executor.initialize();
        return executor;
    }

    /**
     * 处理 {@link org.springframework.scheduling.annotation.Async} 注解标记，返回值类型是 void 的方法未被捕获的异常
     * 如果返回类型是 @{@link java.util.concurrent.Future} 将不会被捕获，由应用程序自身处理
     *
     * @return ignored
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error(ex.getMessage(), ex);
            log.error("exception method: {}", method.getDeclaringClass().getName() + "#" + method.getName());
            log.error("params: {}", params);
        };
    }
}
