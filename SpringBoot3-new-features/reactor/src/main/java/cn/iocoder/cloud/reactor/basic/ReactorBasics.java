package cn.iocoder.cloud.reactor.basic;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Reactor基础概念学习
 * 
 * 本示例演示：
 * 1. Mono和Flux的基本概念
 * 2. 创建Publisher的各种方式
 * 3. 订阅和消费数据
 * 4. 基本的操作符使用
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorBasics {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Reactor基础概念学习 ===");
        
        // 1. Mono基础
        demonstrateMono();
        
        // 2. Flux基础
        demonstrateFlux();
        
        // 3. 创建Publisher的方式
        demonstrateCreation();
        
        // 4. 订阅方式
        demonstrateSubscription();
        
        // 5. 基本操作符
        demonstrateBasicOperators();
        
        // 等待异步操作完成
        Thread.sleep(3000);
        log.info("=== 学习完成 ===");
    }

    /**
     * 演示Mono的基本使用
     * Mono: 表示0或1个元素的异步序列
     */
    private static void demonstrateMono() {
        log.info("\n--- 1. Mono基础演示 ---");
        
        // 1.1 创建包含单个值的Mono
        Mono<String> mono1 = Mono.just("Hello Reactor");
        mono1.subscribe(value -> log.info("Mono值: {}", value));
        
        // 1.2 创建空的Mono
        Mono<String> emptyMono = Mono.empty();
        emptyMono.subscribe(
            value -> log.info("空Mono值: {}", value),
            error -> log.error("空Mono错误: {}", error.getMessage()),
            () -> log.info("空Mono完成")
        );
        
        // 1.3 创建错误的Mono
        Mono<String> errorMono = Mono.error(new RuntimeException("测试错误"));
        errorMono.subscribe(
            value -> log.info("错误Mono值: {}", value),
            error -> log.error("错误Mono捕获: {}", error.getMessage()),
            () -> log.info("错误Mono完成")
        );
        
        // 1.4 延迟创建Mono
        Mono<String> delayMono = Mono.fromSupplier(() -> {
            log.info("延迟创建Mono");
            return "延迟值";
        });
        delayMono.subscribe(value -> log.info("延迟Mono值: {}", value));
    }

    /**
     * 演示Flux的基本使用
     * Flux: 表示0到N个元素的异步序列
     */
    private static void demonstrateFlux() {
        log.info("\n--- 2. Flux基础演示 ---");
        
        // 2.1 创建包含多个值的Flux
        Flux<String> flux1 = Flux.just("A", "B", "C", "D");
        flux1.subscribe(value -> log.info("Flux值: {}", value));
        
        // 2.2 从集合创建Flux
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Flux<Integer> flux2 = Flux.fromIterable(numbers);
        flux2.subscribe(value -> log.info("集合Flux值: {}", value));
        
        // 2.3 创建数字范围Flux
        Flux<Integer> rangeFlux = Flux.range(1, 5);
        rangeFlux.subscribe(value -> log.info("范围Flux值: {}", value));
        
        // 2.4 创建空的Flux
        Flux<String> emptyFlux = Flux.empty();
        emptyFlux.subscribe(
            value -> log.info("空Flux值: {}", value),
            error -> log.error("空Flux错误: {}", error.getMessage()),
            () -> log.info("空Flux完成")
        );
    }

    /**
     * 演示创建Publisher的各种方式
     */
    private static void demonstrateCreation() {
        log.info("\n--- 3. 创建Publisher的方式 ---");
        
        // 3.1 使用generate创建
        Flux<Integer> generateFlux = Flux.generate(
            () -> 0,  // 初始状态
            (state, sink) -> {
                sink.next(state);
                if (state == 3) {
                    sink.complete();
                }
                return state + 1;
            }
        );
        generateFlux.subscribe(value -> log.info("Generate值: {}", value));
        
        // 3.2 使用create创建
        Flux<String> createFlux = Flux.create(sink -> {
            for (int i = 1; i <= 3; i++) {
                sink.next("Create-" + i);
            }
            sink.complete();
        });
        createFlux.subscribe(value -> log.info("Create值: {}", value));
        
        // 3.3 使用interval创建定时Flux（这里只演示概念，不实际运行）
        log.info("定时Flux概念：Flux.interval(Duration.ofSeconds(1)) 每秒发出一个递增数字");
    }

    /**
     * 演示不同的订阅方式
     */
    private static void demonstrateSubscription() {
        log.info("\n--- 4. 订阅方式演示 ---");
        
        Flux<String> flux = Flux.just("订阅1", "订阅2", "订阅3");
        
        // 4.1 简单订阅
        flux.subscribe(value -> log.info("简单订阅: {}", value));
        
        // 4.2 带错误处理的订阅
        flux.subscribe(
            value -> log.info("带错误处理订阅: {}", value),
            error -> log.error("订阅错误: {}", error.getMessage())
        );
        
        // 4.3 完整订阅
        flux.subscribe(
            value -> log.info("完整订阅值: {}", value),
            error -> log.error("完整订阅错误: {}", error.getMessage()),
            () -> log.info("完整订阅完成")
        );
        
        // 4.4 带背压的订阅
        flux.subscribe(
            value -> log.info("背压订阅值: {}", value),
            error -> log.error("背压订阅错误: {}", error.getMessage()),
            () -> log.info("背压订阅完成"),
            subscription -> {
                log.info("背压订阅开始，请求2个元素");
                subscription.request(2);
            }
        );
    }

    /**
     * 演示基本操作符
     */
    private static void demonstrateBasicOperators() {
        log.info("\n--- 5. 基本操作符演示 ---");
        
        // 5.1 map操作符 - 转换元素
        Flux.range(1, 5)
            .map(i -> i * 2)
            .subscribe(value -> log.info("Map结果: {}", value));
        
        // 5.2 filter操作符 - 过滤元素
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0)
            .subscribe(value -> log.info("Filter结果: {}", value));
        
        // 5.3 take操作符 - 取前N个元素
        Flux.range(1, 10)
            .take(3)
            .subscribe(value -> log.info("Take结果: {}", value));
        
        // 5.4 skip操作符 - 跳过前N个元素
        Flux.range(1, 10)
            .skip(7)
            .subscribe(value -> log.info("Skip结果: {}", value));
        
        // 5.5 操作符链式调用
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0)  // 过滤偶数
            .map(i -> "数字: " + i)     // 转换为字符串
            .take(2)                   // 取前2个
            .subscribe(value -> log.info("链式操作结果: {}", value));
    }
}
