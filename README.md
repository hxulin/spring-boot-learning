# SpringBoot 学习和演示项目

## 1、async-annotation（Async 注解的使用）

### 1.1 Spring 的 @Async 注解

该工具提供方便快捷的异步化执行业务的能力，只需要添加一个注解 `@Async` 既可以使你的业务异步执行，这里的异步执行，指的是新开一个线程业务；该注解可以使用到类上，也可以使用在方法上。

- `@EnableAsync` 启用异步化能力注解，推荐该注解配置在 SpringBoot 的 Config 类或者启动类上
- `@Async` 开启异步化模式注解
- `AsyncConfigurer/AsyncConfigurerSupport` 全局配置接口，用于配置自定义异步线程池和异步线程执行异常捕获器
- `AsyncUncaughtExceptionHandler` 异步化运行时全局异常捕获接口
- `AsyncExecutor` 异步化执行线程池，自定义异步执行线程池

### 1.2 使用 @Async 的注意事项

- SpringBootApplication 启动类添加 `@EnableAsync` 注解
- `@Async` 的使用
  - 类或者方法中使用 `@Async` 注解，类上标有该注解表示类中方法都是异步方法，方法上标有该注解表示方法是异步方法
  - `@Async("threadPool")`，threadPool 为自定义线程池，这样可以保证主线程中调用多个异步任务时能更高效的执行

- 在定义异步方法的同一个类中，调用带有 `@Async` 注解方法，该方法则无法异步执行
- 注解的方法必须是 public 方法，不能是 static
- `@Async` 注解的实现和 `@Transactional` 一样，都是基于 Spring 的 AOP，而 AOP 的实现是基于动态代理模式实现的，所以要通过 Spring 容器管理，否则调用方法的是对象本身而不是代理对象

> 更多参看：[https://blog.csdn.net/qq_44695727/article/details/120082934](https://blog.csdn.net/qq_44695727/article/details/120082934)

## 2、thread-pool（SpringBoot 中的线程池）

配置使用 Spring 默认提供的线程池 `ThreadPoolTaskExecutor` 的样例项目，并处理异步任务的异常捕获。

## 3、customize-thread-pool（自定义线程池）

### 3.1 基于 ThreadPoolTaskExecutor 自定义线程池

前置知识拓展，给一个类做增强的方法：

- 继承
- 装饰设计模式（挟天子以令诸侯）

思考点：都是增强 `java.util.concurrent.ThreadPoolExecutor`，Spring 使用装饰设计模式，我们使用继承？

1. 装饰设计模式降低类与类之间的耦合度，可以增 **减** 功能，比继承要灵活。
2. 我们使用继承是为了方便结合 Spring 的 IOC 功能（按类型扫描 Bean）。

本节的自定义线程池继承自 `org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor`，扩展了告警监控的功能。

### 3.2 线程池的监控

#### 3.2.1 监控参数

在项目中使用线程池的时候，一般需要对线程池进行监控，方便出问题的时候进行查看。线程池本身提供了一些方法来获取线程池的运行状态。

- getActiveCount：线程池中正在执行任务的线程数量
- getCompletedTaskCount：线程池已完成的任务数量，该值小于等于 taskCount
- getCorePoolSize：线程池的核心线程数量
- getLargestPoolSize：线程池曾经创建过的最大线程数量，通过这个数据可以知道线程池是否满过，也就是达到了 maximumPoolSize
- getMaximumPoolSize：获取线程池的最大线程数量
- getPoolSize：获取当前线程池中线程数量的大小
- getTaskCount：线程池已经执行的和未执行的任务总数

除了线程池提供的上述已经实现的方法，同时线程池也预留了很多扩展方法。比如在 runWorker 方法里面，在执行任务之前会回调 beforeExecute 方法，执行任务之后会回调 afterExecute 方法，而这些方法默认都是空实现，你可以自己继承 ThreadPoolExecutor 来扩展重写这些方法，来实现自己想要的功能。

> 摘自：[https://www.zhihu.com/question/41937174/answer/2471908876](https://www.zhihu.com/question/41937174/answer/2471908876)

#### 3.2.2 监控方式

线程池的监控分为 2 种类型，一种是在执行任务前后全量统计任务排队时间和执行时间，另外一种是通过定时任务，定时获取活跃线程数，队列中的任务数，核心线程数，最大线程数等数据。

`MonitoredThreadPoolExecutor` 会同时统计这两种类型的数据。如果您不想统计全量任务执行和排队的监控数据，可以使用 `ThreadPoolMonitor.monitor(String name, ThreadPoolExecutor threadPoolExecutor)` 方法，该方法只使用定时任务来监控线程数据。其中，name 需要唯一，threadPoolExecutor 不能是 `MonitoredThreadPoolExecutor` 类型，否则会抛出异常。

> 摘自：[https://www.jianshu.com/p/fd7b235eeb23](https://www.jianshu.com/p/fd7b235eeb23)