package cn.iocoder.cloud.reactor.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Reactor实际应用示例
 * 
 * 本示例模拟一个电商订单处理系统，演示：
 * 1. 订单流处理
 * 2. 异步服务调用
 * 3. 错误处理和重试
 * 4. 并行处理
 * 5. 背压控制
 * 6. 实时监控和统计
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorRealWorldExample {

    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        log.info("=== 电商订单处理系统启动 ===");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // 启动订单处理流程
        processOrderStream()
            .subscribe(
                result -> log.info("订单处理完成: {}", result),
                error -> {
                    log.error("订单处理系统错误: {}", error.getMessage());
                    latch.countDown();
                },
                () -> {
                    log.info("=== 订单处理系统关闭 ===");
                    latch.countDown();
                }
            );
        
        latch.await();
    }

    /**
     * 订单处理主流程
     */
    private static Flux<OrderResult> processOrderStream() {
        return createOrderStream()
            .doOnNext(order -> log.info("接收订单: {}", order))
            .flatMap(order -> 
                processOrder(order)
                    .onErrorResume(error -> {
                        log.error("订单处理失败: {}, 错误: {}", order.getId(), error.getMessage());
                        return Mono.just(new OrderResult(order.getId(), "FAILED", error.getMessage()));
                    })
            )
            .onBackpressureBuffer(50)
            .limitRate(10) // 限制处理速率
            .take(20); // 处理20个订单后停止
    }

    /**
     * 创建订单流
     */
    private static Flux<Order> createOrderStream() {
        List<String> products = Arrays.asList("iPhone", "MacBook", "iPad", "AirPods", "Watch");
        
        return Flux.interval(Duration.ofMillis(200))
            .map(i -> new Order(
                "ORDER-" + (i + 1),
                "USER-" + (random.nextInt(100) + 1),
                products.get(random.nextInt(products.size())),
                random.nextInt(5) + 1,
                (random.nextInt(1000) + 100) * 1.0,
                LocalDateTime.now()
            ));
    }

    /**
     * 处理单个订单
     */
    private static Mono<OrderResult> processOrder(Order order) {
        return validateOrder(order)
            .flatMap(validOrder -> 
                Mono.zip(
                    checkInventory(validOrder),
                    calculatePrice(validOrder),
                    processPayment(validOrder)
                )
                .flatMap(tuple -> {
                    boolean inventoryOk = tuple.getT1();
                    double finalPrice = tuple.getT2();
                    boolean paymentOk = tuple.getT3();
                    
                    if (inventoryOk && paymentOk) {
                        return createShipment(validOrder)
                            .map(shipmentId -> new OrderResult(
                                validOrder.getId(), 
                                "SUCCESS", 
                                "订单处理成功，运单号: " + shipmentId + "，金额: " + finalPrice
                            ));
                    } else {
                        String reason = !inventoryOk ? "库存不足" : "支付失败";
                        return Mono.just(new OrderResult(validOrder.getId(), "FAILED", reason));
                    }
                })
            )
            .subscribeOn(Schedulers.boundedElastic())
            .timeout(Duration.ofSeconds(5))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(100))
                .filter(throwable -> !(throwable instanceof IllegalArgumentException))
            );
    }

    /**
     * 验证订单
     */
    private static Mono<Order> validateOrder(Order order) {
        return Mono.fromCallable(() -> {
            log.debug("验证订单: {}", order.getId());
            
            // 模拟验证逻辑
            if (order.getQuantity() <= 0) {
                throw new IllegalArgumentException("订单数量无效");
            }
            if (order.getPrice() <= 0) {
                throw new IllegalArgumentException("订单价格无效");
            }
            
            // 模拟验证耗时
            Thread.sleep(50);
            return order;
        });
    }

    /**
     * 检查库存
     */
    private static Mono<Boolean> checkInventory(Order order) {
        return Mono.fromCallable(() -> {
            log.debug("检查库存: {} - {}", order.getProduct(), order.getQuantity());
            
            // 模拟库存检查耗时
            Thread.sleep(100);
            
            // 90%的概率有库存
            return random.nextDouble() > 0.1;
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 计算价格
     */
    private static Mono<Double> calculatePrice(Order order) {
        return Mono.fromCallable(() -> {
            log.debug("计算价格: {}", order.getId());
            
            // 模拟价格计算耗时
            Thread.sleep(80);
            
            // 应用折扣
            double discount = random.nextDouble() * 0.2; // 0-20%折扣
            return order.getPrice() * order.getQuantity() * (1 - discount);
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 处理支付
     */
    private static Mono<Boolean> processPayment(Order order) {
        return Mono.fromCallable(() -> {
            log.debug("处理支付: {}", order.getId());
            
            // 模拟支付处理耗时
            Thread.sleep(150);
            
            // 95%的概率支付成功
            boolean success = random.nextDouble() > 0.05;
            if (!success) {
                throw new RuntimeException("支付处理失败");
            }
            return true;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .retryWhen(Retry.backoff(2, Duration.ofMillis(50)));
    }

    /**
     * 创建运单
     */
    private static Mono<String> createShipment(Order order) {
        return Mono.fromCallable(() -> {
            log.debug("创建运单: {}", order.getId());
            
            // 模拟运单创建耗时
            Thread.sleep(120);
            
            return "SHIP-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 订单实体类
     */
    @Data
    @AllArgsConstructor
    static class Order {
        private String id;
        private String userId;
        private String product;
        private int quantity;
        private double price;
        private LocalDateTime createTime;
    }

    /**
     * 订单处理结果
     */
    @Data
    @AllArgsConstructor
    static class OrderResult {
        private String orderId;
        private String status;
        private String message;
    }
}
