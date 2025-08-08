package com.atguigu.study.basic;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * 为什么需要 Reactive Stream？
 * 演示传统编程 vs 响应式编程的区别
 */
@Slf4j
public class WhyReactiveStream {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== 为什么需要 Reactive Stream ===");
        
        // 1. 演示异步非阻塞
        demonstrateNonBlocking();
        
        // 2. 演示背压控制
        demonstrateBackpressure();
        
        // 3. 演示组合性
        demonstrateComposition();
        
        // 等待异步操作完成
        Thread.sleep(10000);
    }

    /**
     * 演示异步非阻塞处理
     */
    private static void demonstrateNonBlocking() {
        log.info("\n--- 1. 异步非阻塞演示 ---");
        
        // 传统阻塞方式
        log.info("传统阻塞方式开始...");
        long start = System.currentTimeMillis();
        
        // 模拟3个耗时操作
        String result1 = blockingOperation("任务1", 1000);
        String result2 = blockingOperation("任务2", 1000);
        String result3 = blockingOperation("任务3", 1000);
        
        long blockingTime = System.currentTimeMillis() - start;
        log.info("阻塞方式完成，耗时: {}ms, 结果: {}, {}, {}", 
                blockingTime, result1, result2, result3);
        
        // 响应式非阻塞方式
        log.info("响应式非阻塞方式开始...");
        start = System.currentTimeMillis();
        
        CountDownLatch latch = new CountDownLatch(1);
        
        Flux.just("任务1", "任务2", "任务3")
            .flatMap(task -> 
                Flux.fromCallable(() -> blockingOperation(task, 1000))
                    .subscribeOn(Schedulers.boundedElastic())
            )
            .collectList()
            .subscribe(results -> {
                long reactiveTime = System.currentTimeMillis() - start;
                log.info("响应式方式完成，耗时: {}ms, 结果: {}", reactiveTime, results);
                latch.countDown();
            });
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 演示背压控制
     */
    private static void demonstrateBackpressure() {
        log.info("\n--- 2. 背压控制演示 ---");
        
        // 快速生产者，慢速消费者
        Flux.range(1, 1000)
            .doOnNext(i -> log.info("生产: {}", i))
            .onBackpressureBuffer(10) // 缓冲区大小为10
            .delayElements(Duration.ofMillis(100)) // 消费者每100ms处理一个
            .subscribe(
                i -> log.info("消费: {}", i),
                error -> log.error("错误: {}", error.getMessage()),
                () -> log.info("背压演示完成")
            );
    }

    /**
     * 演示组合性
     */
    private static void demonstrateComposition() {
        log.info("\n--- 3. 组合性演示 ---");
        
        // 数据流的复杂变换和组合
        Flux<String> dataStream = Flux.just("apple", "banana", "cherry", "date", "elderberry")
            .filter(fruit -> fruit.length() > 4)           // 过滤长度大于4的
            .map(String::toUpperCase)                       // 转大写
            .flatMap(fruit -> 
                Flux.just(fruit + "-PROCESSED")
                    .delayElements(Duration.ofMillis(200))  // 模拟异步处理
            )
            .take(3);                                       // 只取前3个
        
        dataStream.subscribe(
            result -> log.info("处理结果: {}", result),
            error -> log.error("处理错误: {}", error.getMessage()),
            () -> log.info("组合处理完成")
        );
    }

    /**
     * 模拟阻塞操作
     */
    private static String blockingOperation(String taskName, long delayMs) {
        try {
            Thread.sleep(delayMs);
            return taskName + "-完成";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return taskName + "-中断";
        }
    }
}
