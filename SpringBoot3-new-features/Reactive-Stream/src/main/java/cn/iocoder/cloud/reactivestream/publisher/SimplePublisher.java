package cn.iocoder.cloud.reactivestream.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简单的Publisher实现示例
 * 
 * 这个示例展示了如何实现一个基本的Publisher，
 * 它可以发布一系列整数数据，并支持背压控制。
 * 
 * @author iocoder
 */
public class SimplePublisher implements Publisher<Integer> {
    
    private final int maxValue;
    
    public SimplePublisher(int maxValue) {
        this.maxValue = maxValue;
    }
    
    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
        // 创建订阅关系并传递给订阅者
        SimpleSubscription subscription = new SimpleSubscription(subscriber, maxValue);
        subscriber.onSubscribe(subscription);
    }
    
    /**
     * 简单的Subscription实现
     * 负责控制数据流和背压
     */
    private static class SimpleSubscription implements Subscription {
        private final Subscriber<? super Integer> subscriber;
        private final int maxValue;
        private final AtomicLong requested = new AtomicLong(0);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private int currentValue = 1;
        
        public SimpleSubscription(Subscriber<? super Integer> subscriber, int maxValue) {
            this.subscriber = subscriber;
            this.maxValue = maxValue;
        }
        
        @Override
        public void request(long n) {
            if (n <= 0) {
                // 根据规范，非正数请求应该触发错误
                subscriber.onError(new IllegalArgumentException("请求数量必须大于0"));
                return;
            }
            
            if (cancelled.get()) {
                return; // 已取消，不处理请求
            }
            
            // 原子性地增加请求数量
            long previousRequested = requested.getAndAdd(n);
            
            // 如果之前没有未处理的请求，开始发送数据
            if (previousRequested == 0) {
                sendData();
            }
        }
        
        @Override
        public void cancel() {
            cancelled.set(true);
        }
        
        private void sendData() {
            long toSend = requested.get();
            
            while (toSend > 0 && currentValue <= maxValue && !cancelled.get()) {
                try {
                    // 发送数据
                    subscriber.onNext(currentValue);
                    currentValue++;
                    toSend--;
                    
                    // 原子性地减少请求计数
                    requested.decrementAndGet();
                    
                    // 模拟一些处理时间
                    Thread.sleep(100);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    subscriber.onError(e);
                    return;
                } catch (Exception e) {
                    subscriber.onError(e);
                    return;
                }
            }
            
            // 检查是否完成
            if (currentValue > maxValue && !cancelled.get()) {
                subscriber.onComplete();
            }
        }
    }
    
    /**
     * 创建一个发布1到n的整数的Publisher
     */
    public static Publisher<Integer> range(int start, int count) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                RangeSubscription subscription = new RangeSubscription(subscriber, start, count);
                subscriber.onSubscribe(subscription);
            }
        };
    }
    
    /**
     * 范围Publisher的Subscription实现
     */
    private static class RangeSubscription implements Subscription {
        private final Subscriber<? super Integer> subscriber;
        private final int start;
        private final int count;
        private final AtomicLong requested = new AtomicLong(0);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private int index = 0;
        
        public RangeSubscription(Subscriber<? super Integer> subscriber, int start, int count) {
            this.subscriber = subscriber;
            this.start = start;
            this.count = count;
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
            
            long previousRequested = requested.getAndAdd(n);
            if (previousRequested == 0) {
                sendData();
            }
        }
        
        @Override
        public void cancel() {
            cancelled.set(true);
        }
        
        private void sendData() {
            long toSend = requested.get();
            
            while (toSend > 0 && index < count && !cancelled.get()) {
                try {
                    subscriber.onNext(start + index);
                    index++;
                    toSend--;
                    requested.decrementAndGet();
                    
                    // 模拟处理时间
                    Thread.sleep(50);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    subscriber.onError(e);
                    return;
                } catch (Exception e) {
                    subscriber.onError(e);
                    return;
                }
            }
            
            if (index >= count && !cancelled.get()) {
                subscriber.onComplete();
            }
        }
    }
    
    /**
     * 演示Publisher的使用
     */
    public static void demonstratePublisher() {
        System.out.println("=== SimplePublisher 演示 ===");
        
        // 创建一个发布1到5的Publisher
        Publisher<Integer> publisher = new SimplePublisher(5);
        
        // 创建一个简单的订阅者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            private Subscription subscription;
            
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                System.out.println("订阅成功，开始请求数据...");
                // 请求前3个数据
                subscription.request(3);
            }
            
            @Override
            public void onNext(Integer item) {
                System.out.println("接收到数据: " + item);
                
                // 演示背压控制：每接收一个数据后，再请求一个
                if (item < 5) {
                    subscription.request(1);
                }
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.err.println("发生错误: " + throwable.getMessage());
            }
            
            @Override
            public void onComplete() {
                System.out.println("数据流完成！");
            }
        };
        
        // 订阅
        publisher.subscribe(subscriber);
        
        // 等待处理完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Reactive Stream Publisher 实现示例");
        System.out.println("===================================\n");
        
        demonstratePublisher();
        
        System.out.println("\n=== 范围Publisher演示 ===");
        
        // 使用范围Publisher
        Publisher<Integer> rangePublisher = range(10, 5);
        rangePublisher.subscribe(new Subscriber<Integer>() {
            private Subscription subscription;
            
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                System.out.println("范围Publisher订阅成功");
                subscription.request(Long.MAX_VALUE); // 请求所有数据
            }
            
            @Override
            public void onNext(Integer item) {
                System.out.println("范围数据: " + item);
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.err.println("范围Publisher错误: " + throwable.getMessage());
            }
            
            @Override
            public void onComplete() {
                System.out.println("范围Publisher完成！");
            }
        });
        
        // 等待处理完成
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
