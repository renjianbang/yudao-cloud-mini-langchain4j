package cn.iocoder.cloud.reactivestream.pubsub;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reactive Stream 发布订阅模式完整示例
 * 
 * 这个示例展示了：
 * 1. 如何创建自定义的Publisher
 * 2. 如何创建自定义的Subscriber
 * 3. 背压控制的实现
 * 4. 错误处理机制
 * 5. 异步处理
 * 
 * @author iocoder
 */
public class PublishSubscribeExample {
    
    /**
     * 新闻发布者 - 模拟新闻数据源
     */
    public static class NewsPublisher implements Publisher<String> {
        private final String[] news;
        private final ExecutorService executor;
        
        public NewsPublisher(String[] news) {
            this.news = news;
            this.executor = Executors.newSingleThreadExecutor();
        }
        
        @Override
        public void subscribe(Subscriber<? super String> subscriber) {
            NewsSubscription subscription = new NewsSubscription(subscriber, news, executor);
            subscriber.onSubscribe(subscription);
        }
        
        public void shutdown() {
            executor.shutdown();
        }
    }
    
    /**
     * 新闻订阅关系实现
     */
    private static class NewsSubscription implements Subscription {
        private final Subscriber<? super String> subscriber;
        private final String[] news;
        private final ExecutorService executor;
        private final AtomicLong requested = new AtomicLong(0);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private int currentIndex = 0;
        
        public NewsSubscription(Subscriber<? super String> subscriber, String[] news, ExecutorService executor) {
            this.subscriber = subscriber;
            this.news = news;
            this.executor = executor;
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
                // 异步发送数据
                CompletableFuture.runAsync(this::sendNews, executor);
            }
        }
        
        @Override
        public void cancel() {
            cancelled.set(true);
        }
        
        private void sendNews() {
            while (requested.get() > 0 && currentIndex < news.length && !cancelled.get()) {
                try {
                    String newsItem = news[currentIndex];
                    subscriber.onNext(newsItem);
                    currentIndex++;
                    requested.decrementAndGet();
                    
                    // 模拟网络延迟
                    Thread.sleep(200);
                    
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
            if (currentIndex >= news.length && !cancelled.get()) {
                subscriber.onComplete();
            }
        }
    }
    
    /**
     * 新闻订阅者 - 模拟新闻消费者
     */
    public static class NewsSubscriber implements Subscriber<String> {
        private final String subscriberName;
        private Subscription subscription;
        private int receivedCount = 0;
        
        public NewsSubscriber(String subscriberName) {
            this.subscriberName = subscriberName;
        }
        
        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            System.out.println("[" + subscriberName + "] 订阅成功，开始接收新闻");
            // 初始请求2条新闻
            subscription.request(2);
        }
        
        @Override
        public void onNext(String news) {
            receivedCount++;
            System.out.println("[" + subscriberName + "] 接收新闻 #" + receivedCount + ": " + news);
            
            // 模拟处理时间
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 背压控制：处理完一条新闻后，再请求一条
            if (receivedCount < 10) { // 限制最多接收10条新闻
                subscription.request(1);
            } else {
                System.out.println("[" + subscriberName + "] 达到接收上限，取消订阅");
                subscription.cancel();
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("[" + subscriberName + "] 发生错误: " + throwable.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("[" + subscriberName + "] 新闻接收完成，共接收 " + receivedCount + " 条新闻");
        }
    }
    
    /**
     * 慢速订阅者 - 演示背压控制
     */
    public static class SlowNewsSubscriber implements Subscriber<String> {
        private final String subscriberName;
        private Subscription subscription;
        private int receivedCount = 0;
        
        public SlowNewsSubscriber(String subscriberName) {
            this.subscriberName = subscriberName;
        }
        
        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            System.out.println("[" + subscriberName + "] (慢速)订阅成功");
            // 慢速订阅者一次只请求1条新闻
            subscription.request(1);
        }
        
        @Override
        public void onNext(String news) {
            receivedCount++;
            System.out.println("[" + subscriberName + "] (慢速处理) 新闻 #" + receivedCount + ": " + news);
            
            // 模拟慢速处理
            try {
                Thread.sleep(500); // 慢速处理，每条新闻需要500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 处理完后再请求下一条
            if (receivedCount < 5) {
                subscription.request(1);
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("[" + subscriberName + "] (慢速)发生错误: " + throwable.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("[" + subscriberName + "] (慢速)新闻接收完成，共接收 " + receivedCount + " 条新闻");
        }
    }
    
    /**
     * 演示发布订阅模式
     */
    public static void demonstratePublishSubscribe() {
        System.out.println("=== Reactive Stream 发布订阅模式演示 ===\n");
        
        // 准备新闻数据
        String[] newsData = {
            "科技新闻：人工智能技术取得重大突破",
            "体育新闻：世界杯决赛即将开始",
            "财经新闻：股市今日大涨3%",
            "娱乐新闻：知名演员获得国际大奖",
            "社会新闻：新的环保政策正式实施",
            "教育新闻：在线教育平台用户突破千万",
            "健康新闻：新疫苗研发成功",
            "旅游新闻：热门景点重新开放",
            "美食新闻：米其林餐厅评选结果公布",
            "时尚新闻：春季时装周圆满结束"
        };
        
        // 创建新闻发布者
        NewsPublisher publisher = new NewsPublisher(newsData);
        
        try {
            // 创建多个订阅者
            NewsSubscriber subscriber1 = new NewsSubscriber("订阅者A");
            NewsSubscriber subscriber2 = new NewsSubscriber("订阅者B");
            SlowNewsSubscriber slowSubscriber = new SlowNewsSubscriber("慢速订阅者");
            
            System.out.println("开始订阅新闻...\n");
            
            // 订阅新闻
            publisher.subscribe(subscriber1);
            Thread.sleep(100); // 稍微错开订阅时间
            
            publisher.subscribe(subscriber2);
            Thread.sleep(100);
            
            publisher.subscribe(slowSubscriber);
            
            // 等待所有订阅者处理完成
            Thread.sleep(8000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            publisher.shutdown();
        }
    }
    
    /**
     * 演示错误处理
     */
    public static void demonstrateErrorHandling() {
        System.out.println("\n=== 错误处理演示 ===\n");
        
        // 创建一个会产生错误的Publisher
        Publisher<String> errorPublisher = new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    private boolean requested = false;
                    
                    @Override
                    public void request(long n) {
                        if (!requested) {
                            requested = true;
                            // 模拟异步错误
                            CompletableFuture.runAsync(() -> {
                                try {
                                    Thread.sleep(100);
                                    subscriber.onNext("正常数据");
                                    Thread.sleep(100);
                                    // 模拟错误
                                    subscriber.onError(new RuntimeException("模拟的网络错误"));
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    subscriber.onError(e);
                                }
                            });
                        }
                    }
                    
                    @Override
                    public void cancel() {
                        // 取消操作
                    }
                });
            }
        };
        
        // 订阅错误Publisher
        errorPublisher.subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("订阅错误演示Publisher");
                subscription.request(10);
            }
            
            @Override
            public void onNext(String item) {
                System.out.println("接收到数据: " + item);
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.err.println("捕获到错误: " + throwable.getMessage());
                System.out.println("错误处理：记录日志，通知管理员，尝试重连等");
            }
            
            @Override
            public void onComplete() {
                System.out.println("错误演示完成");
            }
        });
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Reactive Stream 发布订阅模式学习");
        System.out.println("==================================");
        
        demonstratePublishSubscribe();
        demonstrateErrorHandling();
        
        System.out.println("\n=== 总结 ===");
        System.out.println("1. Publisher负责发布数据，支持多个订阅者");
        System.out.println("2. Subscriber通过request()方法控制数据接收速度");
        System.out.println("3. 背压控制防止快速生产者压垮慢速消费者");
        System.out.println("4. 异步处理提高系统响应性和吞吐量");
        System.out.println("5. 统一的错误处理机制保证系统稳定性");
    }
}
