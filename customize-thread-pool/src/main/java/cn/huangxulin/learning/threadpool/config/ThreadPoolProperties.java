package cn.huangxulin.learning.threadpool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author hxl
 */
@Getter
@Setter
@ConfigurationProperties("hxl.thread-pool")
public class ThreadPoolProperties {

    /**
     * 是否开启框架提供的线程池
     */
    private boolean enabled;

    /**
     * 线程池维护线程最小数量，默认20
     */
    private int corePoolSize = 20;

    /**
     * 线程池维护线程最大数量，默认200
     */
    private int maxPoolSize = 200;

    /**
     * 队列最大长度
     */
    private int queueCapacity = 1000;

    /**
     * 空闲线程存活时间，默认 300 秒
     */
    private int keepAliveSeconds = 300;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix = "hxl-task-";

    /**
     * 告警阈值，当线程池使用超过 75% 时触发告警
     */
    private float warningFactor = 0.75f;

    /**
     * 告警间隔时间，防止频繁告警
     */
    private Duration warningInterval = Duration.ofSeconds(120);

    /**
     * 线程池销毁之前执行 shutdown 方法
     */
    private boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 线程池在 shutdown/shutdownNow 之后等待时间，默认 3 秒
     */
    private int awaitTerminationSeconds = 3;

}
