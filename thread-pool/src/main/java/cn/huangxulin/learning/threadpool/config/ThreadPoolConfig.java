package cn.huangxulin.learning.threadpool.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

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
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 队列容量
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 活跃时间
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        // 线程名字前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 线程池拒绝策略，当线程池任务满时，新的任务交由调用者线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 告诉线程池，在销毁之前执行 shutdown 方法
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        // shutdown/shutdownNow 之后等待 3 秒
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        executor.initialize();
        return executor;
    }

    /**
     * 处理 {@link Async} 注解标记，返回值类型是 void 的方法未被捕获的异常
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
