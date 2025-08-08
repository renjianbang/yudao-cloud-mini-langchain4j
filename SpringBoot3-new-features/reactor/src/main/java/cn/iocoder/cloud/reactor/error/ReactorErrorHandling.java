package cn.iocoder.cloud.reactor.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reactor错误处理学习
 * 
 * 本示例演示：
 * 1. 基本错误处理：onErrorReturn, onErrorResume
 * 2. 错误继续：onErrorContinue
 * 3. 重试机制：retry, retryWhen
 * 4. 错误映射：onErrorMap
 * 5. 错误处理最佳实践
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorErrorHandling {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Reactor错误处理学习 ===");
        
        // 1. 基本错误处理
        demonstrateBasicErrorHandling();
        
        // 2. 错误继续处理
        demonstrateErrorContinue();
        
        // 3. 重试机制
        demonstrateRetryMechanism();
        
        // 4. 错误映射
        demonstrateErrorMapping();
        
        // 5. 复杂错误处理场景
        demonstrateComplexErrorHandling();
        
        // 等待异步操作完成
        Thread.sleep(3000);
        log.info("=== 错误处理学习完成 ===");
    }

    /**
     * 演示基本错误处理
     */
    private static void demonstrateBasicErrorHandling() {
        log.info("\n--- 1. 基本错误处理演示 ---");
        
        // 1.1 onErrorReturn - 错误时返回默认值
        log.info("1.1 onErrorReturn演示:");
        Flux.just(1, 2, 0, 4)
            .map(i -> 10 / i)
            .onErrorReturn(-1)
            .subscribe(
                value -> log.info("  onErrorReturn结果: {}", value),
                error -> log.error("  onErrorReturn错误: {}", error.getMessage()),
                () -> log.info("  onErrorReturn完成")
            );
        
        // 1.2 onErrorReturn with predicate - 条件错误返回
        log.info("1.2 onErrorReturn条件处理:");
        Flux.just(1, 2, 0, 4)
            .map(i -> {
                if (i == 0) throw new IllegalArgumentException("除数不能为0");
                return 10 / i;
            })
            .onErrorReturn(IllegalArgumentException.class, -999)
            .subscribe(
                value -> log.info("  条件onErrorReturn结果: {}", value),
                error -> log.error("  条件onErrorReturn错误: {}", error.getMessage()),
                () -> log.info("  条件onErrorReturn完成")
            );
        
        // 1.3 onErrorResume - 错误时切换到另一个流
        log.info("1.3 onErrorResume演示:");
        Flux.just("A", "B", "ERROR", "D")
            .map(s -> {
                if ("ERROR".equals(s)) throw new RuntimeException("遇到错误");
                return s.toLowerCase();
            })
            .onErrorResume(error -> {
                log.warn("  检测到错误，切换到备用流: {}", error.getMessage());
                return Flux.just("backup1", "backup2");
            })
            .subscribe(
                value -> log.info("  onErrorResume结果: {}", value),
                error -> log.error("  onErrorResume错误: {}", error.getMessage()),
                () -> log.info("  onErrorResume完成")
            );
    }

    /**
     * 演示错误继续处理
     */
    private static void demonstrateErrorContinue() {
        log.info("\n--- 2. 错误继续处理演示 ---");
        
        // 2.1 onErrorContinue - 跳过错误元素，继续处理
        log.info("2.1 onErrorContinue演示:");
        Flux.just(1, 2, 0, 4, 5)
            .map(i -> {
                log.info("  处理元素: {}", i);
                return 10 / i;
            })
            .onErrorContinue((error, value) -> {
                log.warn("  跳过错误元素: {}, 错误: {}", value, error.getMessage());
            })
            .subscribe(
                value -> log.info("  onErrorContinue结果: {}", value),
                error -> log.error("  onErrorContinue最终错误: {}", error.getMessage()),
                () -> log.info("  onErrorContinue完成")
            );
        
        // 2.2 onErrorContinue with predicate - 条件错误继续
        log.info("2.2 onErrorContinue条件处理:");
        Flux.just("1", "abc", "3", "def", "5")
            .map(s -> {
                log.info("  转换字符串: {}", s);
                return Integer.parseInt(s);
            })
            .onErrorContinue(NumberFormatException.class, (error, value) -> {
                log.warn("  跳过无效数字: {}", value);
            })
            .subscribe(
                value -> log.info("  条件onErrorContinue结果: {}", value),
                error -> log.error("  条件onErrorContinue错误: {}", error.getMessage()),
                () -> log.info("  条件onErrorContinue完成")
            );
    }

    /**
     * 演示重试机制
     */
    private static void demonstrateRetryMechanism() {
        log.info("\n--- 3. 重试机制演示 ---");
        
        // 3.1 简单重试
        log.info("3.1 简单重试演示:");
        AtomicInteger attempt1 = new AtomicInteger(0);
        Mono.fromCallable(() -> {
            int currentAttempt = attempt1.incrementAndGet();
            log.info("  尝试次数: {}", currentAttempt);
            if (currentAttempt < 3) {
                throw new RuntimeException("模拟失败");
            }
            return "成功!";
        })
        .retry(3)
        .subscribe(
            value -> log.info("  简单重试结果: {}", value),
            error -> log.error("  简单重试最终失败: {}", error.getMessage())
        );
        
        // 3.2 条件重试
        log.info("3.2 条件重试演示:");
        AtomicInteger attempt2 = new AtomicInteger(0);
        Mono.fromCallable(() -> {
            int currentAttempt = attempt2.incrementAndGet();
            log.info("  条件重试尝试次数: {}", currentAttempt);
            if (currentAttempt < 2) {
                throw new IllegalStateException("可重试错误");
            } else if (currentAttempt == 2) {
                throw new IllegalArgumentException("不可重试错误");
            }
            return "成功!";
        })
        .retryWhen(Retry.indefinitely()
            .filter(throwable -> throwable instanceof IllegalStateException)
            .doBeforeRetry(retrySignal -> 
                log.info("  准备重试，错误: {}", retrySignal.failure().getMessage()))
        )
        .subscribe(
            value -> log.info("  条件重试结果: {}", value),
            error -> log.error("  条件重试最终失败: {}", error.getMessage())
        );
        
        // 3.3 带延迟的重试
        log.info("3.3 延迟重试演示:");
        AtomicInteger attempt3 = new AtomicInteger(0);
        Mono.fromCallable(() -> {
            int currentAttempt = attempt3.incrementAndGet();
            log.info("  延迟重试尝试次数: {} - 时间: {}", currentAttempt, System.currentTimeMillis() % 10000);
            if (currentAttempt < 3) {
                throw new RuntimeException("需要重试");
            }
            return "延迟重试成功!";
        })
        .retryWhen(Retry.backoff(3, Duration.ofMillis(200))
            .doBeforeRetry(retrySignal -> 
                log.info("  延迟重试，延迟: {}ms", retrySignal.totalRetries() * 200))
        )
        .subscribe(
            value -> log.info("  延迟重试结果: {}", value),
            error -> log.error("  延迟重试最终失败: {}", error.getMessage())
        );
    }

    /**
     * 演示错误映射
     */
    private static void demonstrateErrorMapping() {
        log.info("\n--- 4. 错误映射演示 ---");
        
        // 4.1 onErrorMap - 转换错误类型
        log.info("4.1 onErrorMap演示:");
        Flux.just("valid", "invalid", "data")
            .map(s -> {
                if ("invalid".equals(s)) {
                    throw new IllegalArgumentException("原始错误: " + s);
                }
                return s.toUpperCase();
            })
            .onErrorMap(IllegalArgumentException.class, 
                original -> new RuntimeException("转换后的错误: " + original.getMessage(), original))
            .subscribe(
                value -> log.info("  onErrorMap结果: {}", value),
                error -> log.error("  onErrorMap错误: {} (原因: {})", 
                    error.getMessage(), error.getCause().getMessage()),
                () -> log.info("  onErrorMap完成")
            );
        
        // 4.2 条件错误映射
        log.info("4.2 条件错误映射:");
        Flux.just(1, -1, 2, -2)
            .map(i -> {
                if (i < 0) throw new IllegalArgumentException("负数: " + i);
                return i * 2;
            })
            .onErrorMap(
                error -> error instanceof IllegalArgumentException && error.getMessage().contains("负数"),
                error -> new UnsupportedOperationException("不支持负数操作: " + error.getMessage())
            )
            .subscribe(
                value -> log.info("  条件错误映射结果: {}", value),
                error -> log.error("  条件错误映射错误: {}", error.getMessage()),
                () -> log.info("  条件错误映射完成")
            );
    }

    /**
     * 演示复杂错误处理场景
     */
    private static void demonstrateComplexErrorHandling() {
        log.info("\n--- 5. 复杂错误处理场景演示 ---");
        
        // 模拟复杂的业务处理流程
        Flux.just("order1", "order2", "invalid_order", "order3")
            .flatMap(orderId -> processOrder(orderId)
                .onErrorResume(error -> {
                    log.warn("  订单处理失败: {}, 错误: {}", orderId, error.getMessage());
                    return Mono.just("FAILED_" + orderId);
                })
            )
            .onErrorContinue((error, value) -> {
                log.warn("  跳过处理失败的订单: {}", value);
            })
            .subscribe(
                result -> log.info("  复杂处理结果: {}", result),
                error -> log.error("  复杂处理最终错误: {}", error.getMessage()),
                () -> log.info("  复杂处理完成")
            );
    }

    /**
     * 模拟订单处理方法
     */
    private static Mono<String> processOrder(String orderId) {
        return Mono.fromCallable(() -> {
            log.info("  处理订单: {}", orderId);
            
            // 模拟不同的处理结果
            if ("invalid_order".equals(orderId)) {
                throw new IllegalArgumentException("无效订单");
            }
            
            // 模拟处理时间
            Thread.sleep(100);
            return "PROCESSED_" + orderId;
        });
    }
}
