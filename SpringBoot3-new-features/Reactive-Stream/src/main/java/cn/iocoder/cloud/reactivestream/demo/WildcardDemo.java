package cn.iocoder.cloud.reactivestream.demo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 泛型通配符演示
 * 解释 <? super Integer> 的含义和作用
 * 
 * @author iocoder
 */
public class WildcardDemo {
    
    /**
     * 演示为什么需要 <? super Integer>
     */
    public static void demonstrateWildcard() {
        System.out.println("=== 泛型通配符 <? super Integer> 演示 ===\n");
        
        // 创建一个发布Integer的Publisher
        Publisher<Integer> integerPublisher = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                System.out.println("Publisher接受了订阅者: " + subscriber.getClass().getSimpleName());
                
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        // 发送Integer数据
                        subscriber.onNext(42);
                        subscriber.onNext(100);
                        subscriber.onComplete();
                    }
                    
                    @Override
                    public void cancel() {}
                });
            }
        };
        
        // 1. Integer类型的订阅者 - 完全匹配
        System.out.println("1. Integer类型的订阅者:");
        Subscriber<Integer> integerSubscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("  Integer订阅者: 订阅成功");
                s.request(10);
            }
            
            @Override
            public void onNext(Integer item) {
                System.out.println("  Integer订阅者接收: " + item + " (类型: Integer)");
            }
            
            @Override
            public void onError(Throwable t) {}
            
            @Override
            public void onComplete() {
                System.out.println("  Integer订阅者: 完成\n");
            }
        };
        
        // 2. Number类型的订阅者 - Integer的父类
        System.out.println("2. Number类型的订阅者 (Integer的父类):");
        Subscriber<Number> numberSubscriber = new Subscriber<Number>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("  Number订阅者: 订阅成功");
                s.request(10);
            }
            
            @Override
            public void onNext(Number item) {
                System.out.println("  Number订阅者接收: " + item + " (类型: " + item.getClass().getSimpleName() + ")");
            }
            
            @Override
            public void onError(Throwable t) {}
            
            @Override
            public void onComplete() {
                System.out.println("  Number订阅者: 完成\n");
            }
        };
        
        // 3. Object类型的订阅者 - 所有类的父类
        System.out.println("3. Object类型的订阅者 (所有类的父类):");
        Subscriber<Object> objectSubscriber = new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("  Object订阅者: 订阅成功");
                s.request(10);
            }
            
            @Override
            public void onNext(Object item) {
                System.out.println("  Object订阅者接收: " + item + " (类型: " + item.getClass().getSimpleName() + ")");
            }
            
            @Override
            public void onError(Throwable t) {}
            
            @Override
            public void onComplete() {
                System.out.println("  Object订阅者: 完成\n");
            }
        };
        
        // 测试订阅 - 由于使用了 <? super Integer>，以下都可以成功
        integerPublisher.subscribe(integerSubscriber);  // ✅ 可以
        integerPublisher.subscribe(numberSubscriber);   // ✅ 可以
        integerPublisher.subscribe(objectSubscriber);   // ✅ 可以
        
        // 如果没有通配符，只能接受完全匹配的类型
        // integerPublisher.subscribe(numberSubscriber);   // ❌ 编译错误
        // integerPublisher.subscribe(objectSubscriber);   // ❌ 编译错误
    }
    
    /**
     * 对比演示：如果不使用通配符会怎样
     */
    public static void demonstrateWithoutWildcard() {
        System.out.println("=== 不使用通配符的情况 ===\n");
        
        // 假设Publisher不使用通配符（这只是演示，实际Reactive Streams规范要求使用通配符）
        class StrictPublisher implements Publisher<Integer> {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                System.out.println("严格Publisher: 只能接受特定类型的订阅者");
            }
        }
        
        System.out.println("使用通配符的好处:");
        System.out.println("1. 灵活性: 可以接受Integer及其父类型的订阅者");
        System.out.println("2. 多态性: 支持面向对象的多态特性");
        System.out.println("3. 可扩展性: 便于系统扩展和维护");
        System.out.println("4. 类型安全: 在编译时保证类型安全");
    }
    
    /**
     * 实际应用场景演示
     */
    public static void demonstrateRealWorldScenario() {
        System.out.println("\n=== 实际应用场景 ===\n");
        
        // 场景：数据处理系统
        Publisher<Integer> dataPublisher = subscriber -> {
            System.out.println("数据发布者开始工作...");
            subscriber.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    // 发布一些整数数据
                    for (int i = 1; i <= 3; i++) {
                        subscriber.onNext(i * 10);
                    }
                    subscriber.onComplete();
                }
                
                @Override
                public void cancel() {}
            });
        };
        
        // 通用的数字处理器 - 可以处理任何Number类型
        Subscriber<Number> numberProcessor = new Subscriber<Number>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("数字处理器: 准备处理数字数据");
                s.request(Long.MAX_VALUE);
            }
            
            @Override
            public void onNext(Number number) {
                // 可以对任何Number类型进行通用处理
                double doubled = number.doubleValue() * 2;
                System.out.println("数字处理器: " + number + " -> " + doubled + " (翻倍)");
            }
            
            @Override
            public void onError(Throwable t) {}
            
            @Override
            public void onComplete() {
                System.out.println("数字处理器: 处理完成");
            }
        };
        
        // 由于使用了 <? super Integer>，Integer的Publisher可以被Number的Subscriber订阅
        dataPublisher.subscribe(numberProcessor);
    }
    
    /**
     * 类型关系总结
     */
    public static void summarizeTypeRelationship() {
        System.out.println("\n=== 类型关系总结 ===");
        System.out.println();
        System.out.println("继承层次:");
        System.out.println("Object");
        System.out.println("  ↑");
        System.out.println("Number");
        System.out.println("  ↑");
        System.out.println("Integer");
        System.out.println();
        System.out.println("<? super Integer> 的含义:");
        System.out.println("✅ Subscriber<Integer>  - 可以接受");
        System.out.println("✅ Subscriber<Number>   - 可以接受 (Integer的父类)");
        System.out.println("✅ Subscriber<Object>   - 可以接受 (Integer的父类)");
        System.out.println("❌ Subscriber<String>   - 不能接受 (不是Integer的父类)");
        System.out.println("❌ Subscriber<Double>   - 不能接受 (不是Integer的父类)");
        System.out.println();
        System.out.println("这种设计的优势:");
        System.out.println("1. 类型安全: 编译时检查类型兼容性");
        System.out.println("2. 灵活性: 支持多态和继承");
        System.out.println("3. 可重用性: 一个Publisher可以被多种类型的Subscriber使用");
        System.out.println("4. 符合里氏替换原则: 子类可以替换父类");
    }
    
    public static void main(String[] args) {
        demonstrateWildcard();
        demonstrateWithoutWildcard();
        demonstrateRealWorldScenario();
        summarizeTypeRelationship();
    }
}
