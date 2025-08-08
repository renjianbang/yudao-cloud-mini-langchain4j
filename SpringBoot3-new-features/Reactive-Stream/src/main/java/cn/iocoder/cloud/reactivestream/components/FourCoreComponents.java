package cn.iocoder.cloud.reactivestream.components;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.Processor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * Reactive Stream 四大核心组件详解与实现
 * 
 * 1. Publisher<T> - 发布者
 * 2. Subscriber<T> - 订阅者  
 * 3. Subscription - 订阅关系
 * 4. Processor<T,R> - 处理器
 * 
 * @author iocoder
 */
public class FourCoreComponents {
    
    /**
     * 1. Publisher 组件详解
     * 
     * Publisher是数据流的源头，负责：
     * - 提供数据
     * - 管理订阅者
     * - 控制数据发送
     */
    public static class DataPublisher<T> implements Publisher<T> {
        private final T[] data;
        
        @SafeVarargs
        public DataPublisher(T... data) {
            this.data = data;
        }
        
        @Override
        public void subscribe(Subscriber<? super T> subscriber) {
            System.out.println("Publisher: 新的订阅者加入");
            DataSubscription<T> subscription = new DataSubscription<>(subscriber, data);
            subscriber.onSubscribe(subscription);
        }
        
        /**
         * Publisher的职责说明
         */
        public static void explainPublisherRole() {
            System.out.println("=== Publisher 组件职责 ===");
            System.out.println("1. 数据源管理：维护要发布的数据");
            System.out.println("2. 订阅管理：处理订阅者的订阅请求");
            System.out.println("3. 生命周期管理：控制数据流的开始和结束");
            System.out.println("4. 多播支持：可以支持多个订阅者同时订阅");
            System.out.println();
        }
    }
    
    /**
     * 2. Subscription 组件详解
     * 
     * Subscription是Publisher和Subscriber之间的桥梁，负责：
     * - 背压控制
     * - 取消机制
     * - 数据传输控制
     */
    public static class DataSubscription<T> implements Subscription {
        private final Subscriber<? super T> subscriber;
        private final T[] data;
        private final AtomicLong requested = new AtomicLong(0);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private int currentIndex = 0;
        
        public DataSubscription(Subscriber<? super T> subscriber, T[] data) {
            this.subscriber = subscriber;
            this.data = data;
        }
        
        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("请求数量必须大于0"));
                return;
            }
            
            if (cancelled.get()) {
                return;
            }
            
            System.out.println("Subscription: 收到请求 " + n + " 个数据项");
            
            long previousRequested = requested.getAndAdd(n);
            if (previousRequested == 0) {
                sendData();
            }
        }
        
        @Override
        public void cancel() {
            System.out.println("Subscription: 订阅被取消");
            cancelled.set(true);
        }
        
        private void sendData() {
            while (requested.get() > 0 && currentIndex < data.length && !cancelled.get()) {
                T item = data[currentIndex];
                System.out.println("Subscription: 发送数据 " + item);
                subscriber.onNext(item);
                currentIndex++;
                requested.decrementAndGet();
                
                // 模拟处理延迟
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    subscriber.onError(e);
                    return;
                }
            }
            
            if (currentIndex >= data.length && !cancelled.get()) {
                System.out.println("Subscription: 数据发送完成");
                subscriber.onComplete();
            }
        }
        
        /**
         * Subscription的职责说明
         */
        public static void explainSubscriptionRole() {
            System.out.println("=== Subscription 组件职责 ===");
            System.out.println("1. 背压控制：根据订阅者请求控制数据发送速度");
            System.out.println("2. 取消机制：提供取消订阅的能力");
            System.out.println("3. 流量控制：防止数据积压和内存溢出");
            System.out.println("4. 状态管理：维护订阅的状态信息");
            System.out.println();
        }
    }
    
    /**
     * 3. Subscriber 组件详解
     * 
     * Subscriber是数据的消费者，负责：
     * - 接收数据
     * - 处理错误
     * - 控制数据流速度
     */
    public static class DataSubscriber<T> implements Subscriber<T> {
        private final String name;
        private Subscription subscription;
        private int receivedCount = 0;
        
        public DataSubscriber(String name) {
            this.name = name;
        }
        
        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            System.out.println("Subscriber[" + name + "]: 订阅成功");
            // 初始请求
            subscription.request(2);
        }
        
        @Override
        public void onNext(T item) {
            receivedCount++;
            System.out.println("Subscriber[" + name + "]: 接收数据 #" + receivedCount + " = " + item);
            
            // 模拟数据处理
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 背压控制：处理完后请求更多数据
            if (receivedCount < 10) {
                subscription.request(1);
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("Subscriber[" + name + "]: 发生错误 - " + throwable.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("Subscriber[" + name + "]: 接收完成，共处理 " + receivedCount + " 个数据项");
        }
        
        /**
         * Subscriber的职责说明
         */
        public static void explainSubscriberRole() {
            System.out.println("=== Subscriber 组件职责 ===");
            System.out.println("1. 数据消费：接收并处理来自Publisher的数据");
            System.out.println("2. 背压控制：通过request()方法控制数据接收速度");
            System.out.println("3. 错误处理：处理数据流中的异常情况");
            System.out.println("4. 生命周期管理：管理订阅的生命周期");
            System.out.println();
        }
    }
    
    /**
     * 4. Processor 组件详解
     * 
     * Processor既是Publisher又是Subscriber，负责：
     * - 数据转换
     * - 中间处理
     * - 链式操作
     */
    public static class DataProcessor<T, R> implements Processor<T, R> {
        private final Function<T, R> transformer;
        private Subscriber<? super R> downstream;
        private Subscription upstream;
        
        public DataProcessor(Function<T, R> transformer) {
            this.transformer = transformer;
        }
        
        // 作为Publisher的方法
        @Override
        public void subscribe(Subscriber<? super R> subscriber) {
            System.out.println("Processor: 下游订阅者连接");
            this.downstream = subscriber;
            
            // 创建一个代理Subscription
            downstream.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    System.out.println("Processor: 向上游请求 " + n + " 个数据项");
                    if (upstream != null) {
                        upstream.request(n);
                    }
                }
                
                @Override
                public void cancel() {
                    System.out.println("Processor: 取消上游订阅");
                    if (upstream != null) {
                        upstream.cancel();
                    }
                }
            });
        }
        
        // 作为Subscriber的方法
        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println("Processor: 连接到上游Publisher");
            this.upstream = subscription;
        }
        
        @Override
        public void onNext(T item) {
            System.out.println("Processor: 接收上游数据 " + item);
            try {
                // 数据转换
                R transformedItem = transformer.apply(item);
                System.out.println("Processor: 转换后数据 " + transformedItem);
                
                // 发送给下游
                if (downstream != null) {
                    downstream.onNext(transformedItem);
                }
            } catch (Exception e) {
                onError(e);
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("Processor: 处理错误 - " + throwable.getMessage());
            if (downstream != null) {
                downstream.onError(throwable);
            }
        }
        
        @Override
        public void onComplete() {
            System.out.println("Processor: 上游完成");
            if (downstream != null) {
                downstream.onComplete();
            }
        }
        
        /**
         * Processor的职责说明
         */
        public static void explainProcessorRole() {
            System.out.println("=== Processor 组件职责 ===");
            System.out.println("1. 数据转换：将输入类型T转换为输出类型R");
            System.out.println("2. 中间处理：在数据流中进行过滤、映射、聚合等操作");
            System.out.println("3. 链式连接：连接上游Publisher和下游Subscriber");
            System.out.println("4. 背压传播：将下游的背压信号传播到上游");
            System.out.println();
        }
    }
    
    /**
     * 演示四大组件的协作
     */
    public static void demonstrateFourComponents() {
        System.out.println("=== 四大核心组件协作演示 ===\n");
        
        // 1. 创建Publisher（数据源）
        DataPublisher<Integer> publisher = new DataPublisher<>(1, 2, 3, 4, 5);
        
        // 2. 创建Processor（数据转换：整数转换为字符串）
        DataProcessor<Integer, String> processor = new DataProcessor<>(
            num -> "数字:" + num + "(平方:" + (num * num) + ")"
        );
        
        // 3. 创建Subscriber（数据消费者）
        DataSubscriber<String> subscriber = new DataSubscriber<>("最终消费者");
        
        // 4. 建立连接：Publisher -> Processor -> Subscriber
        System.out.println("建立数据流连接...\n");
        
        // 连接Processor到Subscriber
        processor.subscribe(subscriber);
        
        // 连接Publisher到Processor
        publisher.subscribe(processor);
        
        // 等待处理完成
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Reactive Stream 四大核心组件学习");
        System.out.println("=================================\n");
        
        // 解释各组件职责
        DataPublisher.explainPublisherRole();
        DataSubscription.explainSubscriptionRole();
        DataSubscriber.explainSubscriberRole();
        DataProcessor.explainProcessorRole();
        
        // 演示组件协作
        demonstrateFourComponents();
        
        System.out.println("\n=== 总结 ===");
        System.out.println("1. Publisher：数据源，负责发布数据");
        System.out.println("2. Subscriber：数据消费者，负责处理数据");
        System.out.println("3. Subscription：连接桥梁，负责背压控制");
        System.out.println("4. Processor：数据处理器，负责转换和中间处理");
        System.out.println("\n这四个组件协同工作，构成了完整的响应式数据流处理体系！");
    }
}
