package cn.iocoder.cloud.reactivestream.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Project Reactor 实际应用示例
 * 
 * Project Reactor是Reactive Stream规范的一个实现，
 * 提供了Mono和Flux两个核心类型：
 * - Mono<T>：表示0或1个元素的异步序列
 * - Flux<T>：表示0到N个元素的异步序列
 * 
 * @author iocoder
 */
public class ProjectReactorExample {
    
    /**
     * Mono 基础用法演示
     */
    public static void demonstrateMono() {
        System.out.println("=== Mono 基础用法演示 ===");
        
        // 1. 创建Mono
        Mono<String> mono1 = Mono.just("Hello Reactive");
        Mono<String> mono2 = Mono.fromSupplier(() -> "Hello from Supplier");
        Mono<String> emptyMono = Mono.empty();
        Mono<String> errorMono = Mono.error(new RuntimeException("Something went wrong"));
        
        // 2. 订阅和消费
        System.out.println("1. 基本订阅：");
        mono1.subscribe(
            data -> System.out.println("接收到数据: " + data),
            error -> System.err.println("发生错误: " + error.getMessage()),
            () -> System.out.println("完成")
        );
        
        // 3. 数据转换
        System.out.println("\n2. 数据转换：");
        mono2
            .map(String::toUpperCase)
            .map(s -> s + "!")
            .subscribe(data -> System.out.println("转换后: " + data));
        
        // 4. 错误处理
        System.out.println("\n3. 错误处理：");
        errorMono
            .onErrorReturn("默认值")
            .subscribe(data -> System.out.println("错误恢复: " + data));
        
        // 5. 空值处理
        System.out.println("\n4. 空值处理：");
        emptyMono
            .defaultIfEmpty("空值时的默认值")
            .subscribe(data -> System.out.println("空值处理: " + data));
        
        System.out.println();
    }
    
    /**
     * Flux 基础用法演示
     */
    public static void demonstrateFlux() {
        System.out.println("=== Flux 基础用法演示 ===");
        
        // 1. 创建Flux
        Flux<Integer> flux1 = Flux.just(1, 2, 3, 4, 5);
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"));
        Flux<Long> flux3 = Flux.range(1, 5).map(i -> (long) i);
        
        // 2. 基本订阅
        System.out.println("1. 基本订阅：");
        flux1.subscribe(
            data -> System.out.println("数据: " + data),
            error -> System.err.println("错误: " + error.getMessage()),
            () -> System.out.println("Flux完成")
        );
        
        // 3. 数据转换和过滤
        System.out.println("\n2. 数据转换和过滤：");
        flux2
            .filter(s -> !s.equals("B"))  // 过滤掉B
            .map(s -> s.toLowerCase())     // 转换为小写
            .map(s -> "字母:" + s)         // 添加前缀
            .subscribe(data -> System.out.println(data));
        
        // 4. 数据聚合
        System.out.println("\n3. 数据聚合：");
        flux3
            .reduce(0L, Long::sum)  // 求和
            .subscribe(sum -> System.out.println("总和: " + sum));
        
        System.out.println();
    }
    
    /**
     * 背压控制演示
     */
    public static void demonstrateBackpressure() {
        System.out.println("=== 背压控制演示 ===");
        
        // 创建一个快速生产数据的Flux
        Flux<Integer> fastProducer = Flux.range(1, 1000)
            .delayElements(Duration.ofMillis(1)); // 每1ms产生一个数据
        
        // 慢速消费者
        fastProducer
            .onBackpressureBuffer(10)  // 缓冲区大小为10
            .subscribe(
                data -> {
                    try {
                        Thread.sleep(100); // 慢速处理，每个数据需要100ms
                        System.out.println("慢速处理: " + data);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                },
                error -> System.err.println("背压错误: " + error.getMessage()),
                () -> System.out.println("背压演示完成")
            );
        
        // 等待一段时间观察效果
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * 异步处理演示
     */
    public static void demonstrateAsyncProcessing() {
        System.out.println("=== 异步处理演示 ===");
        
        // 模拟异步数据处理
        Flux<String> asyncFlux = Flux.just("任务1", "任务2", "任务3", "任务4")
            .flatMap(task -> 
                Mono.fromCallable(() -> {
                    // 模拟异步处理
                    Thread.sleep(1000);
                    return "完成: " + task;
                })
                .subscribeOn(Schedulers.boundedElastic()) // 在弹性线程池中执行
            );
        
        System.out.println("开始异步处理...");
        long startTime = System.currentTimeMillis();
        
        asyncFlux.subscribe(
            result -> {
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("[" + elapsed + "ms] " + result);
            },
            error -> System.err.println("异步处理错误: " + error.getMessage()),
            () -> {
                long totalTime = System.currentTimeMillis() - startTime;
                System.out.println("异步处理完成，总耗时: " + totalTime + "ms");
            }
        );
        
        // 等待异步处理完成
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * 组合操作演示
     */
    public static void demonstrateComposition() {
        System.out.println("=== 组合操作演示 ===");
        
        // 创建两个数据源
        Flux<String> source1 = Flux.just("A", "B", "C");
        Flux<String> source2 = Flux.just("1", "2", "3");
        
        // 1. 合并操作
        System.out.println("1. 合并操作：");
        Flux.merge(source1, source2)
            .subscribe(data -> System.out.println("合并: " + data));
        
        // 2. 压缩操作
        System.out.println("\n2. 压缩操作：");
        Flux.zip(source1, source2)
            .map(tuple -> tuple.getT1() + tuple.getT2())
            .subscribe(data -> System.out.println("压缩: " + data));
        
        // 3. 连接操作
        System.out.println("\n3. 连接操作：");
        source1.concatWith(source2)
            .subscribe(data -> System.out.println("连接: " + data));
        
        System.out.println();
    }
    
    /**
     * 错误处理演示
     */
    public static void demonstrateErrorHandling() {
        System.out.println("=== 错误处理演示 ===");
        
        Flux<Integer> errorFlux = Flux.just(1, 2, 0, 4, 5)
            .map(i -> {
                if (i == 0) {
                    throw new RuntimeException("除零错误");
                }
                return 10 / i;
            });
        
        // 1. 错误恢复
        System.out.println("1. 错误恢复：");
        errorFlux
            .onErrorReturn(-1)  // 发生错误时返回-1
            .subscribe(
                data -> System.out.println("结果: " + data),
                error -> System.err.println("错误: " + error.getMessage()),
                () -> System.out.println("错误恢复完成")
            );
        
        // 2. 错误继续
        System.out.println("\n2. 错误继续：");
        errorFlux
            .onErrorContinue((throwable, obj) -> {
                System.out.println("跳过错误数据: " + obj + ", 错误: " + throwable.getMessage());
            })
            .subscribe(
                data -> System.out.println("正常结果: " + data),
                error -> System.err.println("最终错误: " + error.getMessage()),
                () -> System.out.println("错误继续完成")
            );
        
        System.out.println();
    }
    
    /**
     * 实际应用场景：模拟用户数据处理
     */
    public static void demonstrateRealWorldScenario() {
        System.out.println("=== 实际应用场景：用户数据处理 ===");
        
        // 模拟用户数据
        List<User> users = Arrays.asList(
            new User(1, "张三", "zhang@example.com", 25),
            new User(2, "李四", "li@example.com", 30),
            new User(3, "王五", "wang@example.com", 22),
            new User(4, "赵六", "zhao@example.com", 35)
        );
        
        // 响应式处理用户数据
        Flux.fromIterable(users)
            .filter(user -> user.getAge() >= 25)  // 过滤年龄
            .map(user -> user.getName() + "(" + user.getEmail() + ")")  // 转换格式
            .flatMap(userInfo -> 
                // 模拟异步发送邮件
                Mono.fromCallable(() -> {
                    Thread.sleep(500); // 模拟网络延迟
                    return "邮件已发送给: " + userInfo;
                })
                .subscribeOn(Schedulers.boundedElastic())
            )
            .subscribe(
                result -> System.out.println(result),
                error -> System.err.println("处理错误: " + error.getMessage()),
                () -> System.out.println("用户数据处理完成")
            );
        
        // 等待异步处理完成
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 用户数据模型
     */
    public static class User {
        private final int id;
        private final String name;
        private final String email;
        private final int age;
        
        public User(int id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
    }
    
    public static void main(String[] args) {
        System.out.println("Project Reactor 实际应用示例");
        System.out.println("============================\n");
        
        demonstrateMono();
        demonstrateFlux();
        demonstrateBackpressure();
        demonstrateAsyncProcessing();
        demonstrateComposition();
        demonstrateErrorHandling();
        demonstrateRealWorldScenario();
        
        System.out.println("=== Project Reactor 总结 ===");
        System.out.println("1. Mono：处理0或1个元素的异步序列");
        System.out.println("2. Flux：处理0到N个元素的异步序列");
        System.out.println("3. 背压控制：自动处理生产者和消费者速度不匹配");
        System.out.println("4. 异步处理：提高系统并发性和响应性");
        System.out.println("5. 丰富的操作符：支持各种数据转换和组合操作");
    }
}
