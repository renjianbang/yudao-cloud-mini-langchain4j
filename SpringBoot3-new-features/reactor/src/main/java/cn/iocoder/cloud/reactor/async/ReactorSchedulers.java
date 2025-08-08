package cn.iocoder.cloud.reactor.async;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * Reactor异步和调度器学习
 * 
 * 本示例演示：
 * 1. 不同类型的Schedulers
 * 2. subscribeOn vs publishOn
 * 3. 异步操作的线程切换
 * 4. 并行处理
 * 5. 延迟和定时操作
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorSchedulers {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Reactor异步和调度器学习 ===");
        
        // 1. 不同类型的调度器
        demonstrateSchedulerTypes();
        
        // 2. subscribeOn vs publishOn
        demonstrateSubscribeOnVsPublishOn();
        
        // 3. 异步操作
        demonstrateAsyncOperations();
        
        // 4. 并行处理
        demonstrateParallelProcessing();
        
        // 5. 延迟和定时操作
        demonstrateDelayAndTiming();
        
        // 等待异步操作完成
        Thread.sleep(8000);
        log.info("=== 调度器学习完成 ===");
    }

    /**
     * 演示不同类型的调度器
     */
    private static void demonstrateSchedulerTypes() throws InterruptedException {
        log.info("\n--- 1. 调度器类型演示 ---");
        
        CountDownLatch latch = new CountDownLatch(5);
        
        // 1.1 immediate() - 当前线程立即执行
        log.info("1.1 immediate调度器:");
        Mono.just("immediate")
            .subscribeOn(Schedulers.immediate())
            .subscribe(value -> {
                log.info("  immediate - 线程: {}, 值: {}", Thread.currentThread().getName(), value);
                latch.countDown();
            });
        
        // 1.2 single() - 单线程调度器
        log.info("1.2 single调度器:");
        Mono.just("single")
            .subscribeOn(Schedulers.single())
            .subscribe(value -> {
                log.info("  single - 线程: {}, 值: {}", Thread.currentThread().getName(), value);
                latch.countDown();
            });
        
        // 1.3 boundedElastic() - 有界弹性调度器（适合I/O操作）
        log.info("1.3 boundedElastic调度器:");
        Mono.just("boundedElastic")
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(value -> {
                log.info("  boundedElastic - 线程: {}, 值: {}", Thread.currentThread().getName(), value);
                latch.countDown();
            });
        
        // 1.4 parallel() - 并行调度器（适合CPU密集型操作）
        log.info("1.4 parallel调度器:");
        Mono.just("parallel")
            .subscribeOn(Schedulers.parallel())
            .subscribe(value -> {
                log.info("  parallel - 线程: {}, 值: {}", Thread.currentThread().getName(), value);
                latch.countDown();
            });
        
        // 1.5 newSingle() - 新建单线程调度器
        log.info("1.5 newSingle调度器:");
        Mono.just("newSingle")
            .subscribeOn(Schedulers.newSingle("custom-single"))
            .subscribe(value -> {
                log.info("  newSingle - 线程: {}, 值: {}", Thread.currentThread().getName(), value);
                latch.countDown();
            });
        
        latch.await();
    }

    /**
     * 演示subscribeOn vs publishOn的区别
     */
    private static void demonstrateSubscribeOnVsPublishOn() throws InterruptedException {
        log.info("\n--- 2. subscribeOn vs publishOn演示 ---");
        
        CountDownLatch latch = new CountDownLatch(2);
        
        // 2.1 subscribeOn - 影响整个链的执行线程
        log.info("2.1 subscribeOn演示:");
        Flux.just("A", "B", "C")
            .doOnNext(value -> log.info("  subscribeOn源 - 线程: {}, 值: {}", 
                Thread.currentThread().getName(), value))
            .subscribeOn(Schedulers.boundedElastic())
            .map(value -> {
                log.info("  subscribeOn map - 线程: {}, 值: {}", 
                    Thread.currentThread().getName(), value);
                return value.toLowerCase();
            })
            .subscribe(
                value -> log.info("  subscribeOn订阅 - 线程: {}, 值: {}", 
                    Thread.currentThread().getName(), value),
                error -> log.error("错误: {}", error.getMessage()),
                () -> {
                    log.info("  subscribeOn完成");
                    latch.countDown();
                }
            );
        
        // 2.2 publishOn - 只影响下游操作的执行线程
        log.info("2.2 publishOn演示:");
        Flux.just("X", "Y", "Z")
            .doOnNext(value -> log.info("  publishOn源 - 线程: {}, 值: {}", 
                Thread.currentThread().getName(), value))
            .publishOn(Schedulers.parallel())
            .map(value -> {
                log.info("  publishOn map - 线程: {}, 值: {}", 
                    Thread.currentThread().getName(), value);
                return value.toLowerCase();
            })
            .subscribe(
                value -> log.info("  publishOn订阅 - 线程: {}, 值: {}", 
                    Thread.currentThread().getName(), value),
                error -> log.error("错误: {}", error.getMessage()),
                () -> {
                    log.info("  publishOn完成");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 演示异步操作
     */
    private static void demonstrateAsyncOperations() throws InterruptedException {
        log.info("\n--- 3. 异步操作演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 模拟异步任务
        Flux.range(1, 5)
            .flatMap(i -> 
                Mono.fromCallable(() -> {
                    // 模拟耗时操作
                    Thread.sleep(100);
                    return "任务" + i + "完成";
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(result -> log.info("  异步任务结果: {} - 线程: {}", 
                    result, Thread.currentThread().getName()))
            )
            .subscribe(
                value -> log.info("  最终结果: {}", value),
                error -> log.error("异步操作错误: {}", error.getMessage()),
                () -> {
                    log.info("  所有异步任务完成");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 演示并行处理
     */
    private static void demonstrateParallelProcessing() throws InterruptedException {
        log.info("\n--- 4. 并行处理演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 并行处理大量数据
        Flux.range(1, 10)
            .parallel(4) // 使用4个并行轨道
            .runOn(Schedulers.parallel()) // 在parallel调度器上运行
            .map(i -> {
                log.info("  并行处理: {} - 线程: {}", i, Thread.currentThread().getName());
                // 模拟CPU密集型操作
                return i * i;
            })
            .sequential() // 重新合并为顺序流
            .subscribe(
                value -> log.info("  并行结果: {}", value),
                error -> log.error("并行处理错误: {}", error.getMessage()),
                () -> {
                    log.info("  并行处理完成");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 演示延迟和定时操作
     */
    private static void demonstrateDelayAndTiming() throws InterruptedException {
        log.info("\n--- 5. 延迟和定时操作演示 ---");
        
        CountDownLatch latch = new CountDownLatch(3);
        
        // 5.1 delay - 延迟发射
        log.info("5.1 delay延迟操作:");
        Mono.just("延迟消息")
            .delayElement(Duration.ofMillis(500))
            .subscribe(
                value -> {
                    log.info("  延迟结果: {} - 时间: {}", value, System.currentTimeMillis() % 10000);
                    latch.countDown();
                }
            );
        
        // 5.2 delaySequence - 延迟整个序列
        log.info("5.2 delaySequence延迟序列:");
        Flux.just("消息1", "消息2")
            .delaySequence(Duration.ofMillis(800))
            .subscribe(
                value -> log.info("  延迟序列结果: {} - 时间: {}", value, System.currentTimeMillis() % 10000),
                error -> log.error("错误: {}", error.getMessage()),
                () -> {
                    log.info("  延迟序列完成");
                    latch.countDown();
                }
            );
        
        // 5.3 timeout - 超时处理
        log.info("5.3 timeout超时处理:");
        Mono.just("快速响应")
            .delayElement(Duration.ofMillis(200))
            .timeout(Duration.ofMillis(1000))
            .subscribe(
                value -> {
                    log.info("  超时测试结果: {}", value);
                    latch.countDown();
                },
                error -> {
                    log.error("  超时错误: {}", error.getMessage());
                    latch.countDown();
                }
            );
        
        latch.await();
    }
}
