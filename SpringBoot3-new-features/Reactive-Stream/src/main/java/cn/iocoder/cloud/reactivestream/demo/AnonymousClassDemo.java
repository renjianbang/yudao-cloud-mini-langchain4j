package cn.iocoder.cloud.reactivestream.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 匿名内部类演示
 * 展示不同的Subscriber实现方式
 * 
 * @author iocoder
 */
public class AnonymousClassDemo {
    
    // ========== 方式1：传统的具名类实现 ==========
    
    /**
     * 传统方式：创建一个具名的类来实现Subscriber接口
     */
    static class NamedSubscriber implements Subscriber<Integer> {
        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("具名类: 订阅成功");
            s.request(5);
        }
        
        @Override
        public void onNext(Integer item) {
            System.out.println("具名类: 接收数据 " + item);
        }
        
        @Override
        public void onError(Throwable t) {
            System.err.println("具名类: 发生错误 " + t.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("具名类: 完成");
        }
    }
    
    // ========== 方式2：匿名内部类实现 ==========
    
    public static void demonstrateAnonymousClass() {
        System.out.println("=== 匿名内部类演示 ===\n");
        
        // 这就是你问的匿名内部类写法！
        Subscriber<Integer> anonymousSubscriber = new Subscriber<Integer>() {
            // 注意：这里是匿名内部类的开始 {
            
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("匿名类: 订阅成功");
                s.request(5);
            }
            
            @Override
            public void onNext(Integer item) {
                System.out.println("匿名类: 接收数据 " + item);
            }
            
            @Override
            public void onError(Throwable t) {
                System.err.println("匿名类: 发生错误 " + t.getMessage());
            }
            
            @Override
            public void onComplete() {
                System.out.println("匿名类: 完成");
            }
            
        }; // 注意：这里是匿名内部类的结束 }; 
        
        System.out.println("匿名内部类创建完成");
        System.out.println("匿名类的实际类名: " + anonymousSubscriber.getClass().getName());
        System.out.println("匿名类是否为匿名类: " + anonymousSubscriber.getClass().isAnonymousClass());
    }
    
    // ========== 方式3：Lambda表达式（函数式接口才能用） ==========
    
    public static void demonstrateLambda() {
        System.out.println("\n=== Lambda表达式对比 ===\n");
        
        // 注意：Subscriber不是函数式接口（有多个抽象方法），所以不能用Lambda
        // 但我们可以演示函数式接口的Lambda写法
        
        // 假设有一个函数式接口
        @FunctionalInterface
        interface SimpleCallback {
            void onData(Integer data);
        }
        
        // 传统匿名内部类写法
        SimpleCallback callback1 = new SimpleCallback() {
            @Override
            public void onData(Integer data) {
                System.out.println("匿名类回调: " + data);
            }
        };
        
        // Lambda表达式写法（只适用于函数式接口）
        SimpleCallback callback2 = (data) -> {
            System.out.println("Lambda回调: " + data);
        };
        
        // 更简洁的Lambda写法
        SimpleCallback callback3 = data -> System.out.println("简洁Lambda: " + data);
        
        // 测试调用
        callback1.onData(100);
        callback2.onData(200);
        callback3.onData(300);
        
        System.out.println("\n注意：Subscriber有4个抽象方法，不是函数式接口，不能用Lambda！");
    }
    
    // ========== 方式4：方法引用（如果适用） ==========
    
    public static void demonstrateMethodReference() {
        System.out.println("\n=== 方法引用演示 ===\n");
        
        // 创建一个具名类实例
        NamedSubscriber namedInstance = new NamedSubscriber();
        
        // 如果有合适的方法，可以使用方法引用
        // 但Subscriber接口太复杂，通常不适用
        
        System.out.println("方法引用通常用于简单的函数式接口");
        System.out.println("Subscriber接口太复杂，一般使用匿名内部类或具名类");
    }
    
    // ========== 语法详解 ==========
    
    public static void explainSyntax() {
        System.out.println("\n=== 匿名内部类语法详解 ===\n");
        
        System.out.println("语法结构分析:");
        System.out.println("Subscriber<Integer> subscriber = new Subscriber<Integer>() {");
        System.out.println("    ^1                ^2              ^3                ^4  ^5");
        System.out.println("    |                 |               |                 |   |");
        System.out.println("    |                 |               |                 |   +-- 匿名类开始");
        System.out.println("    |                 |               |                 +-- 构造器调用");
        System.out.println("    |                 |               +-- 接口/类名");
        System.out.println("    |                 +-- new关键字");
        System.out.println("    +-- 变量声明");
        System.out.println();
        System.out.println("    // 这里实现接口的所有抽象方法");
        System.out.println("    @Override");
        System.out.println("    public void onSubscribe(Subscription s) { ... }");
        System.out.println("    // ... 其他方法");
        System.out.println("};  <-- 匿名类结束，注意分号！");
        System.out.println();
        
        System.out.println("关键特点:");
        System.out.println("1. 没有类名 - 编译器会自动生成类名（如：OuterClass$1）");
        System.out.println("2. 必须实现接口的所有抽象方法");
        System.out.println("3. 可以访问外部类的成员（包括final变量）");
        System.out.println("4. 编译后会生成单独的.class文件");
        System.out.println("5. 适用于一次性使用的简单实现");
    }
    
    // ========== 实际应用场景 ==========
    
    public static void demonstrateUseCases() {
        System.out.println("\n=== 实际应用场景 ===\n");
        
        System.out.println("何时使用匿名内部类:");
        System.out.println("✅ 接口实现只用一次");
        System.out.println("✅ 实现逻辑相对简单");
        System.out.println("✅ 不需要复用");
        System.out.println("✅ 快速原型开发");
        System.out.println();
        
        System.out.println("何时使用具名类:");
        System.out.println("✅ 实现逻辑复杂");
        System.out.println("✅ 需要复用");
        System.out.println("✅ 需要继承或被继承");
        System.out.println("✅ 需要多个构造器");
        System.out.println();
        
        System.out.println("Reactive Stream中的典型用法:");
        System.out.println("- 快速创建测试用的Subscriber");
        System.out.println("- 简单的数据处理逻辑");
        System.out.println("- 原型开发和演示代码");
    }
    
    public static void main(String[] args) {
        demonstrateAnonymousClass();
        demonstrateLambda();
        demonstrateMethodReference();
        explainSyntax();
        demonstrateUseCases();
        
        System.out.println("\n=== 总结 ===");
        System.out.println("new Subscriber<>() { ... } 确实是匿名内部类！");
        System.out.println("这是Java中实现接口的一种便捷方式。");
    }
}
