package cn.iocoder.cloud.reactivestream.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reactive Stream 综合应用示例
 * 
 * 这个示例模拟了一个实际的业务场景：
 * 电商系统的订单处理流程，展示了Reactive Stream在实际项目中的应用
 * 
 * @author iocoder
 */
public class ReactiveStreamApplication {
    
    private static final Random random = new Random();
    private static final AtomicInteger orderIdGenerator = new AtomicInteger(1000);
    
    /**
     * 订单数据模型
     */
    public static class Order {
        private final int orderId;
        private final String customerId;
        private final List<String> products;
        private final double amount;
        private final LocalDateTime createTime;
        private OrderStatus status;
        
        public Order(String customerId, List<String> products, double amount) {
            this.orderId = orderIdGenerator.incrementAndGet();
            this.customerId = customerId;
            this.products = products;
            this.amount = amount;
            this.createTime = LocalDateTime.now();
            this.status = OrderStatus.CREATED;
        }
        
        // Getters and setters
        public int getOrderId() { return orderId; }
        public String getCustomerId() { return customerId; }
        public List<String> getProducts() { return products; }
        public double getAmount() { return amount; }
        public LocalDateTime getCreateTime() { return createTime; }
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
        
        @Override
        public String toString() {
            return String.format("Order{id=%d, customer='%s', amount=%.2f, status=%s}", 
                orderId, customerId, amount, status);
        }
    }
    
    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        CREATED, VALIDATED, PAYMENT_PROCESSED, SHIPPED, DELIVERED, CANCELLED
    }
    
    /**
     * 订单服务 - 使用Reactive Stream处理订单
     */
    public static class OrderService {
        
        /**
         * 创建订单流
         */
        public Flux<Order> createOrderStream() {
            return Flux.interval(Duration.ofSeconds(1))
                .take(10) // 创建10个订单
                .map(i -> {
                    String customerId = "customer_" + (i % 3 + 1);
                    List<String> products = Arrays.asList("商品A", "商品B", "商品C")
                        .subList(0, random.nextInt(3) + 1);
                    double amount = 100 + random.nextDouble() * 900;
                    return new Order(customerId, products, amount);
                })
                .doOnNext(order -> System.out.println("📝 创建订单: " + order));
        }
        
        /**
         * 验证订单
         */
        public Mono<Order> validateOrder(Order order) {
            return Mono.fromCallable(() -> {
                // 模拟验证逻辑
                Thread.sleep(200 + random.nextInt(300));
                
                // 10%的概率验证失败
                if (random.nextDouble() < 0.1) {
                    throw new RuntimeException("订单验证失败: 库存不足");
                }
                
                order.setStatus(OrderStatus.VALIDATED);
                System.out.println("✅ 订单验证成功: " + order.getOrderId());
                return order;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                System.err.println("❌ 订单验证失败: " + order.getOrderId() + " - " + error.getMessage());
                order.setStatus(OrderStatus.CANCELLED);
                return Mono.just(order);
            });
        }
        
        /**
         * 处理支付
         */
        public Mono<Order> processPayment(Order order) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                return Mono.just(order);
            }
            
            return Mono.fromCallable(() -> {
                // 模拟支付处理
                Thread.sleep(500 + random.nextInt(500));
                
                // 5%的概率支付失败
                if (random.nextDouble() < 0.05) {
                    throw new RuntimeException("支付处理失败: 银行卡余额不足");
                }
                
                order.setStatus(OrderStatus.PAYMENT_PROCESSED);
                System.out.println("💳 支付处理成功: " + order.getOrderId() + " - ¥" + String.format("%.2f", order.getAmount()));
                return order;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                System.err.println("❌ 支付处理失败: " + order.getOrderId() + " - " + error.getMessage());
                order.setStatus(OrderStatus.CANCELLED);
                return Mono.just(order);
            });
        }
        
        /**
         * 发货处理
         */
        public Mono<Order> shipOrder(Order order) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                return Mono.just(order);
            }
            
            return Mono.fromCallable(() -> {
                // 模拟发货处理
                Thread.sleep(300 + random.nextInt(200));
                
                order.setStatus(OrderStatus.SHIPPED);
                System.out.println("🚚 订单发货: " + order.getOrderId());
                return order;
            })
            .subscribeOn(Schedulers.boundedElastic());
        }
        
        /**
         * 完整的订单处理流程
         */
        public Flux<Order> processOrderPipeline() {
            return createOrderStream()
                .flatMap(order -> 
                    validateOrder(order)
                        .flatMap(this::processPayment)
                        .flatMap(this::shipOrder)
                        .onErrorResume(error -> {
                            System.err.println("❌ 订单处理失败: " + order.getOrderId() + " - " + error.getMessage());
                            order.setStatus(OrderStatus.CANCELLED);
                            return Mono.just(order);
                        })
                , 3) // 最多并发处理3个订单
                .doOnNext(order -> System.out.println("✨ 订单处理完成: " + order))
                .doOnComplete(() -> System.out.println("🎉 所有订单处理完成"));
        }
    }
    
    /**
     * 订单统计服务
     */
    public static class OrderStatisticsService {
        
        public void generateStatistics(Flux<Order> orderFlux) {
            System.out.println("\n📊 开始生成订单统计...");
            
            // 统计总订单数
            orderFlux
                .count()
                .subscribe(count -> System.out.println("📈 总订单数: " + count));
            
            // 统计成功订单数
            orderFlux
                .filter(order -> order.getStatus() == OrderStatus.SHIPPED)
                .count()
                .subscribe(count -> System.out.println("✅ 成功订单数: " + count));
            
            // 统计取消订单数
            orderFlux
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .count()
                .subscribe(count -> System.out.println("❌ 取消订单数: " + count));
            
            // 统计总金额
            orderFlux
                .filter(order -> order.getStatus() == OrderStatus.SHIPPED)
                .map(Order::getAmount)
                .reduce(0.0, Double::sum)
                .subscribe(total -> System.out.println("💰 总成交金额: ¥" + String.format("%.2f", total)));
            
            // 按客户统计
            orderFlux
                .filter(order -> order.getStatus() == OrderStatus.SHIPPED)
                .groupBy(Order::getCustomerId)
                .flatMap(group -> 
                    group.count()
                        .map(count -> group.key() + ": " + count + "个订单")
                )
                .subscribe(stat -> System.out.println("👤 客户统计: " + stat));
        }
    }
    
    /**
     * 实时监控服务
     */
    public static class OrderMonitoringService {
        
        public void startMonitoring(Flux<Order> orderFlux) {
            System.out.println("\n🔍 开始实时监控...");
            
            // 监控高价值订单
            orderFlux
                .filter(order -> order.getAmount() > 500)
                .subscribe(order -> 
                    System.out.println("🔥 高价值订单警报: " + order.getOrderId() + " - ¥" + String.format("%.2f", order.getAmount()))
                );
            
            // 监控失败订单
            orderFlux
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .subscribe(order -> 
                    System.out.println("⚠️ 订单失败警报: " + order.getOrderId() + " - " + order.getCustomerId())
                );
            
            // 监控处理时间
            orderFlux
                .window(Duration.ofSeconds(5))
                .flatMap(window -> 
                    window.count()
                        .map(count -> "⏱️ 最近5秒处理订单数: " + count)
                )
                .subscribe(System.out::println);
        }
    }
    
    /**
     * 主应用程序
     */
    public static void runApplication() {
        System.out.println("🚀 启动Reactive Stream电商订单处理系统");
        System.out.println("===========================================\n");
        
        OrderService orderService = new OrderService();
        OrderStatisticsService statisticsService = new OrderStatisticsService();
        OrderMonitoringService monitoringService = new OrderMonitoringService();
        
        // 创建订单处理流
        Flux<Order> orderFlux = orderService.processOrderPipeline()
            .share(); // 共享流，允许多个订阅者
        
        // 启动监控
        monitoringService.startMonitoring(orderFlux);
        
        // 处理订单并生成统计
        orderFlux
            .doOnComplete(() -> {
                // 延迟生成统计，确保所有订单都处理完成
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                statisticsService.generateStatistics(orderService.processOrderPipeline());
            })
            .subscribe();
        
        // 等待所有处理完成
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n🏁 系统运行完成");
    }
    
    public static void main(String[] args) {
        runApplication();
        
        System.out.println("\n=== Reactive Stream 应用总结 ===");
        System.out.println("1. 异步非阻塞处理：提高系统吞吐量");
        System.out.println("2. 背压控制：防止系统过载");
        System.out.println("3. 错误处理：优雅处理异常情况");
        System.out.println("4. 流式处理：实时处理数据流");
        System.out.println("5. 组合操作：灵活组合各种处理逻辑");
        System.out.println("6. 资源高效：更好的资源利用率");
    }
}
