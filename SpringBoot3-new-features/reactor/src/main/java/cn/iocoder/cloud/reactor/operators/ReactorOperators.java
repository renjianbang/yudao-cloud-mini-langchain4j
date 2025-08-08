package cn.iocoder.cloud.reactor.operators;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Reactor操作符学习
 * 
 * 本示例演示：
 * 1. 转换操作符：map, flatMap, cast, index
 * 2. 过滤操作符：filter, distinct, take, skip
 * 3. 组合操作符：merge, zip, concat
 * 4. 聚合操作符：reduce, collect, count
 * 5. 条件操作符：all, any, hasElements
 * 
 * @author iocoder
 */
@Slf4j
public class ReactorOperators {

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Reactor操作符学习 ===");
        
        // 1. 转换操作符
        demonstrateTransformOperators();
        
        // 2. 过滤操作符
        demonstrateFilterOperators();
        
        // 3. 组合操作符
        demonstrateCombineOperators();
        
        // 4. 聚合操作符
        demonstrateAggregateOperators();
        
        // 5. 条件操作符
        demonstrateConditionalOperators();
        
        // 等待异步操作完成
        Thread.sleep(2000);
        log.info("=== 操作符学习完成 ===");
    }

    /**
     * 演示转换操作符
     */
    private static void demonstrateTransformOperators() {
        log.info("\n--- 1. 转换操作符演示 ---");
        
        // 1.1 map - 一对一转换
        log.info("1.1 map操作符:");
        Flux.just("apple", "banana", "cherry")
            .map(String::toUpperCase)
            .subscribe(value -> log.info("  map结果: {}", value));
        
        // 1.2 flatMap - 一对多转换，扁平化
        log.info("1.2 flatMap操作符:");
        Flux.just("hello", "world")
            .flatMap(word -> Flux.fromArray(word.split("")))
            .subscribe(value -> log.info("  flatMap结果: {}", value));
        
        // 1.3 concatMap - 有序的flatMap
        log.info("1.3 concatMap操作符:");
        Flux.just("AB", "CD")
            .concatMap(word -> Flux.fromArray(word.split("")))
            .subscribe(value -> log.info("  concatMap结果: {}", value));
        
        // 1.4 cast - 类型转换
        log.info("1.4 cast操作符:");
        Flux.just(1, 2, 3)
            .cast(Number.class)
            .subscribe(value -> log.info("  cast结果: {} (类型: {})", value, value.getClass().getSimpleName()));
        
        // 1.5 index - 添加索引
        log.info("1.5 index操作符:");
        Flux.just("A", "B", "C")
            .index()
            .subscribe(tuple -> log.info("  index结果: 索引={}, 值={}", tuple.getT1(), tuple.getT2()));
    }

    /**
     * 演示过滤操作符
     */
    private static void demonstrateFilterOperators() {
        log.info("\n--- 2. 过滤操作符演示 ---");
        
        // 2.1 filter - 条件过滤
        log.info("2.1 filter操作符:");
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0)
            .subscribe(value -> log.info("  filter结果: {}", value));
        
        // 2.2 distinct - 去重
        log.info("2.2 distinct操作符:");
        Flux.just(1, 2, 2, 3, 3, 3, 4)
            .distinct()
            .subscribe(value -> log.info("  distinct结果: {}", value));
        
        // 2.3 take - 取前N个
        log.info("2.3 take操作符:");
        Flux.range(1, 10)
            .take(3)
            .subscribe(value -> log.info("  take结果: {}", value));
        
        // 2.4 takeLast - 取后N个
        log.info("2.4 takeLast操作符:");
        Flux.range(1, 5)
            .takeLast(2)
            .subscribe(value -> log.info("  takeLast结果: {}", value));
        
        // 2.5 skip - 跳过前N个
        log.info("2.5 skip操作符:");
        Flux.range(1, 5)
            .skip(2)
            .subscribe(value -> log.info("  skip结果: {}", value));
        
        // 2.6 skipLast - 跳过后N个
        log.info("2.6 skipLast操作符:");
        Flux.range(1, 5)
            .skipLast(2)
            .subscribe(value -> log.info("  skipLast结果: {}", value));
    }

    /**
     * 演示组合操作符
     */
    private static void demonstrateCombineOperators() {
        log.info("\n--- 3. 组合操作符演示 ---");
        
        // 3.1 merge - 合并多个流（无序）
        log.info("3.1 merge操作符:");
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("1", "2", "3");
        Flux.merge(flux1, flux2)
            .subscribe(value -> log.info("  merge结果: {}", value));
        
        // 3.2 concat - 连接多个流（有序）
        log.info("3.2 concat操作符:");
        Flux<String> flux3 = Flux.just("X", "Y");
        Flux<String> flux4 = Flux.just("Z");
        Flux.concat(flux3, flux4)
            .subscribe(value -> log.info("  concat结果: {}", value));
        
        // 3.3 zip - 配对组合
        log.info("3.3 zip操作符:");
        Flux<String> names = Flux.just("Alice", "Bob", "Charlie");
        Flux<Integer> ages = Flux.just(25, 30, 35);
        Flux.zip(names, ages)
            .subscribe(tuple -> log.info("  zip结果: 姓名={}, 年龄={}", tuple.getT1(), tuple.getT2()));
        
        // 3.4 zipWith - 与另一个流配对
        log.info("3.4 zipWith操作符:");
        Flux.just("苹果", "香蕉", "橙子")
            .zipWith(Flux.just(5, 3, 8))
            .subscribe(tuple -> log.info("  zipWith结果: 水果={}, 数量={}", tuple.getT1(), tuple.getT2()));
        
        // 3.5 combineLatest - 组合最新值
        log.info("3.5 combineLatest操作符:");
        Flux<String> source1 = Flux.just("A", "B");
        Flux<Integer> source2 = Flux.just(1, 2, 3);
        Flux.combineLatest(source1, source2, (s, i) -> s + i)
            .subscribe(value -> log.info("  combineLatest结果: {}", value));
    }

    /**
     * 演示聚合操作符
     */
    private static void demonstrateAggregateOperators() {
        log.info("\n--- 4. 聚合操作符演示 ---");
        
        // 4.1 reduce - 归约操作
        log.info("4.1 reduce操作符:");
        Flux.range(1, 5)
            .reduce((a, b) -> a + b)
            .subscribe(value -> log.info("  reduce求和结果: {}", value));
        
        // 4.2 reduce with initial value - 带初始值的归约
        log.info("4.2 reduce带初始值:");
        Flux.just("A", "B", "C")
            .reduce("开始", (acc, item) -> acc + "-" + item)
            .subscribe(value -> log.info("  reduce拼接结果: {}", value));
        
        // 4.3 collect - 收集到集合
        log.info("4.3 collect操作符:");
        Flux.just("apple", "banana", "cherry")
            .collectList()
            .subscribe(list -> log.info("  collect列表结果: {}", list));
        
        // 4.4 count - 计数
        log.info("4.4 count操作符:");
        Flux.just("A", "B", "C", "D")
            .count()
            .subscribe(count -> log.info("  count结果: {}", count));
        
        // 4.5 min/max - 最小值/最大值
        log.info("4.5 min/max操作符:");
//        Flux.just(3, 1, 4, 1, 5, 9, 2, 6)
//            .min(Integer::compareTo)
//            .subscribe(min -> log.info("  min结果: {}", min));
//
//        Flux.just(3, 1, 4, 1, 5, 9, 2, 6)
//            .max(Integer::compareTo)
//            .subscribe(max -> log.info("  max结果: {}", max));
    }

    /**
     * 演示条件操作符
     */
    private static void demonstrateConditionalOperators() {
        log.info("\n--- 5. 条件操作符演示 ---");
        
        // 5.1 all - 所有元素都满足条件
        log.info("5.1 all操作符:");
        Flux.just(2, 4, 6, 8)
            .all(i -> i % 2 == 0)
            .subscribe(result -> log.info("  all偶数结果: {}", result));
        
        // 5.2 any - 任意元素满足条件
        log.info("5.2 any操作符:");
        Flux.just(1, 3, 5, 6)
            .any(i -> i % 2 == 0)
            .subscribe(result -> log.info("  any偶数结果: {}", result));
        
        // 5.3 hasElements - 是否有元素
        log.info("5.3 hasElements操作符:");
        Flux.just("A", "B")
            .hasElements()
            .subscribe(result -> log.info("  hasElements结果: {}", result));
        
        Flux.empty()
            .hasElements()
            .subscribe(result -> log.info("  空流hasElements结果: {}", result));
        
        // 5.4 defaultIfEmpty - 空时提供默认值
        log.info("5.4 defaultIfEmpty操作符:");
        Flux.<String>empty()
            .defaultIfEmpty("默认值")
            .subscribe(value -> log.info("  defaultIfEmpty结果: {}", value));
        
        // 5.5 switchIfEmpty - 空时切换到另一个流
        log.info("5.5 switchIfEmpty操作符:");
        Flux.<String>empty()
            .switchIfEmpty(Flux.just("备用1", "备用2"))
            .subscribe(value -> log.info("  switchIfEmpty结果: {}", value));
    }
}
