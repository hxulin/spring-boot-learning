package cn.huangxulin.learning.threadpool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义线程池
 * <p>
 * 可以做一些增强操作，如线程池快用尽的时候，做一些告警机制
 *
 * @author hxl
 */
@Slf4j
public class CustomizeThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final long serialVersionUID = 1L;

    /**
     * 线程池属性
     */
    private final ThreadPoolProperties properties;

    /**
     * 最近一次告警时间戳
     */
    private final AtomicLong lastWarningTime = new AtomicLong(0);

    public CustomizeThreadPoolTaskExecutor(ThreadPoolProperties properties) {
        // 核心线程池大小
        this.setCorePoolSize(properties.getCorePoolSize());
        // 最大线程数
        this.setMaxPoolSize(properties.getMaxPoolSize());
        // 队列容量
        this.setQueueCapacity(properties.getQueueCapacity());
        // 活跃时间
        this.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        // 线程名字前缀
        this.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 线程池拒绝策略，当线程池任务满时，新的任务交由调用者线程处理
        this.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 告诉线程池，在销毁之前执行 shutdown 方法
        this.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        // shutdown/shutdownNow 之后等待 3 秒
        this.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        this.properties = properties;
    }

    /**
     * 检查预警
     */
    private void checkWarning() {
        if (properties.getWarningFactor() > 0) {
            int activeCount = this.getActiveCount();
            int poolSize = this.getPoolSize();
            int queueSize = this.getThreadPoolExecutor().getQueue().size();
            float warningQueueSize = properties.getQueueCapacity() * properties.getWarningFactor();

            System.out.println("activeCount=" + activeCount + ", poolSize=" + poolSize + ", queueSize=" + queueSize);

            if (queueSize >= warningQueueSize) {
                long lastTimestamp = lastWarningTime.get();
                long currentTimestamp = System.currentTimeMillis();
                if (currentTimestamp - lastTimestamp > properties.getWarningInterval().toMillis()) {
                    if (lastWarningTime.compareAndSet(lastTimestamp, currentTimestamp)) {
                        log.error("请注意，你的线程池队列使用量已超过：{}%", properties.getWarningFactor() * 100);
                    }
                }
            }
        }
    }

    @Override
    public void execute(Runnable task) {
        super.execute(task);
        this.checkWarning();
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(task, startTimeout);
        this.checkWarning();
    }

    @Override
    public Future<?> submit(Runnable task) {
        Future<?> future = super.submit(task);
        this.checkWarning();
        return future;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Future<T> future = super.submit(task);
        this.checkWarning();
        return future;
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        ListenableFuture<?> future = super.submitListenable(task);
        this.checkWarning();
        return future;
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        ListenableFuture<T> future = super.submitListenable(task);
        this.checkWarning();
        return future;
    }
}
