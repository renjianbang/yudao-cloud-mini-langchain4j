package cn.iocoder.cloud.reactor.backpressure;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * Reactor背压控制学习
 * 
 * 本示例演示：
 * 1. 背压的概念和问题
 * 2. 背压处理策略：buffer, drop, latest, error
 * 3. 手动背压控制
 * 4. 限流操作：limitRate
 * 5. 实际应用场景
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorBackpressure {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Reactor背压控制学习 ===");
        
        // 1. 背压问题演示
        demonstrateBackpressureProblem();
        
        // 2. 背压处理策略
        demonstrateBackpressureStrategies();
        
        // 3. 手动背压控制
        demonstrateManualBackpressure();
        
        // 4. 限流操作
        demonstrateLimitRate();
        
        // 5. 实际应用场景
        demonstrateRealWorldScenario();
        
        // 等待异步操作完成
        Thread.sleep(8000);
        log.info("=== 背压控制学习完成 ===");
    }

    /**
     * 演示背压问题
     */
    private static void demonstrateBackpressureProblem() throws InterruptedException {
        log.info("\n--- 1. 背压问题演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 快速生产者，慢速消费者
        log.info("1.1 快速生产vs慢速消费:");
        Flux.range(1, 100)
            .doOnNext(i -> log.info("  生产: {}", i))
            .publishOn(Schedulers.boundedElastic())
            .delayElements(Duration.ofMillis(100)) // 消费者每100ms处理一个
            .take(5) // 只取前5个，避免日志过多
            .subscribe(
                i -> log.info("  消费: {}", i),
                error -> {
                    log.error("  背压问题错误: {}", error.getMessage());
                    latch.countDown();
                },
                () -> {
                    log.info("  背压问题演示完成");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 演示背压处理策略
     */
    private static void demonstrateBackpressureStrategies() throws InterruptedException {
        log.info("\n--- 2. 背压处理策略演示 ---");
        
        CountDownLatch latch = new CountDownLatch(4);
        
        // 2.1 BUFFER策略 - 缓冲所有元素
        log.info("2.1 BUFFER策略:");
        Flux.create(sink -> {
            for (int i = 1; i <= 10; i++) {
                log.info("  BUFFER生产: {}", i);
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.BUFFER)
        .onBackpressureBuffer(5) // 缓冲区大小为5
        .delayElements(Duration.ofMillis(200))
        .subscribe(
            i -> log.info("  BUFFER消费: {}", i),
            error -> {
                log.error("  BUFFER错误: {}", error.getMessage());
                latch.countDown();
            },
            () -> {
                log.info("  BUFFER策略完成");
                latch.countDown();
            }
        );
        
        // 2.2 DROP策略 - 丢弃新元素
        log.info("2.2 DROP策略:");
        Flux.create(sink -> {
            for (int i = 1; i <= 10; i++) {
                log.info("  DROP生产: {}", i);
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.DROP)
        .onBackpressureDrop(dropped -> log.warn("  DROP丢弃: {}", dropped))
        .delayElements(Duration.ofMillis(300))
        .subscribe(
            i -> log.info("  DROP消费: {}", i),
            error -> {
                log.error("  DROP错误: {}", error.getMessage());
                latch.countDown();
            },
            () -> {
                log.info("  DROP策略完成");
                latch.countDown();
            }
        );
        
        // 2.3 LATEST策略 - 保留最新元素
        log.info("2.3 LATEST策略:");
        Flux.create(sink -> {
            for (int i = 1; i <= 10; i++) {
                log.info("  LATEST生产: {}", i);
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.LATEST)
        .onBackpressureLatest()
        .delayElements(Duration.ofMillis(400))
        .subscribe(
            i -> log.info("  LATEST消费: {}", i),
            error -> {
                log.error("  LATEST错误: {}", error.getMessage());
                latch.countDown();
            },
            () -> {
                log.info("  LATEST策略完成");
                latch.countDown();
            }
        );
        
        // 2.4 ERROR策略 - 抛出错误
        log.info("2.4 ERROR策略:");
        Flux.create(sink -> {
            for (int i = 1; i <= 5; i++) {
                log.info("  ERROR生产: {}", i);
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.ERROR)
        .onBackpressureError()
        .delayElements(Duration.ofMillis(500))
        .subscribe(
            i -> log.info("  ERROR消费: {}", i),
            error -> {
                log.error("  ERROR策略错误: {}", error.getMessage());
                latch.countDown();
            },
            () -> {
                log.info("  ERROR策略完成");
                latch.countDown();
            }
        );
        
        latch.await();
    }

    /**
     * 演示手动背压控制
     */
    private static void demonstrateManualBackpressure() throws InterruptedException {
        log.info("\n--- 3. 手动背压控制演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 手动控制请求数量
        Flux.range(1, 20)
            .doOnNext(i -> log.info("  手动背压生产: {}", i))
            .subscribe(
                i -> log.info("  手动背压消费: {}", i),
                error -> {
                    log.error("  手动背压错误: {}", error.getMessage());
                    latch.countDown();
                },
                () -> {
                    log.info("  手动背压完成");
                    latch.countDown();
                },
                subscription -> {
                    log.info("  手动背压：初始请求3个元素");
                    subscription.request(3);
                    
                    // 模拟后续请求
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            log.info("  手动背压：请求更多5个元素");
                            subscription.request(5);
                            
                            Thread.sleep(1000);
                            log.info("  手动背压：请求剩余所有元素");
                            subscription.request(Long.MAX_VALUE);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }
            );
        
        latch.await();
    }

    /**
     * 演示限流操作
     */
    private static void demonstrateLimitRate() throws InterruptedException {
        log.info("\n--- 4. 限流操作演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 使用limitRate限制请求速率
        Flux.range(1, 20)
            .doOnNext(i -> log.info("  限流生产: {}", i))
            .limitRate(3) // 每次最多请求3个元素
            .delayElements(Duration.ofMillis(100))
            .subscribe(
                i -> log.info("  限流消费: {}", i),
                error -> {
                    log.error("  限流错误: {}", error.getMessage());
                    latch.countDown();
                },
                () -> {
                    log.info("  限流操作完成");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 演示实际应用场景
     */
    private static void demonstrateRealWorldScenario() throws InterruptedException {
        log.info("\n--- 5. 实际应用场景演示 ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
//        // 模拟文件处理场景：快速读取文件，慢速处理
//        simulateFileProcessing()
//            .onBackpressureBuffer(10,
//                dropped -> log.warn("  文件处理缓冲区满，丢弃: {}", dropped))
//            .limitRate(3) // 限制处理速率
//            .subscribe(
//                result -> log.info("  文件处理结果: {}", result),
//                error -> {
//                    log.error("  文件处理错误: {}", error.getMessage());
//                    latch.countDown();
//                },
//                () -> {
//                    log.info("  文件处理完成");
//                    latch.countDown();
//                }
//            );
//
//        latch.await();
    }

    /**
     * 模拟文件处理流程
     */
//    private static Flux<String> simulateFileProcessing() {
//        return Flux.range(1, 15)
//            .map(i -> "文件" + i + ".txt")
//            .doOnNext(filename -> log.info("  读取文件: {}", filename))
//            .flatMap(filename ->
//                processFile(filename)
//                    .subscribeOn(Schedulers.boundedElastic())
//            );
//    }

    /**
     * 模拟文件处理方法
     */
//    private static Flux<String> processFile(String filename) {
//        return Flux.fromCallable(() -> {
//            // 模拟文件处理耗时
//            Thread.sleep(200);
//            return "已处理-" + filename;
//        });
//    }
}
