package cn.iocoder.cloud.chatgptreactivestreams;

import cn.iocoder.cloud.reactivestream.demo.AnonymousClassDemo;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @Description 这段是最原始的用法，帮助理解 Publisher → Subscriber → Subscription 的交互。
 * @Author cisz
 * @CreateTime 2025-08-08 09:35
 */
/**
 * 简单的Reactive Stream示例
 *
 * 这个示例展示了Reactive Stream规范的四大核心组件：
 * 1. Publisher - 数据发布者
 * 2. Subscriber - 数据订阅者
 * 3. Subscription - 订阅关系（控制数据流）
 * 4. 背压控制机制
 *
 * @author iocoder
 */
public class SimpleReactiveStream {
    public static void main(String[] args) {
        // ========== 1. 创建Publisher（数据发布者） ==========
        Publisher<Integer> publisher = new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                // 当有订阅者订阅时，创建一个Subscription对象
                // 这是Publisher和Subscriber之间的桥梁
                subscriber.onSubscribe(new Subscription() {
                    // 数据计数器，用于生成递增的整数
                    int count = 0;
                    // 取消标志，用于控制数据流的停止
                    boolean canceled = false;

                    /**
                     * 背压控制的核心方法
                     * 订阅者通过这个方法告诉发布者需要多少数据
                     * @param n 请求的数据数量
                     */
                    @Override
                    public void request(long n) {
                        // 根据请求数量发送数据，同时检查是否被取消
                        for (int i = 0; i < n && !canceled; i++) {
                            // 发送下一个数据项（递增的整数）
                            subscriber.onNext(++count);

                            // 当发送到第5个数据时，完成数据流
                            if (count == 5) {
                                subscriber.onComplete();
                                break; // 跳出循环，停止发送数据
                            }
                        }
                    }

                    /**
                     * 取消订阅的方法
                     * 订阅者可以随时调用此方法来停止数据流
                     */
                    @Override
                    public void cancel() {
                        canceled = true; // 设置取消标志
                        System.out.println("订阅已取消");
                    }
                });
            }
        };

        // ========== 2. 创建Subscriber（数据订阅者） ==========
        Subscriber<Integer> subscriber = new Subscriber<>() {
            // 保存订阅关系的引用，用于后续的数据请求
            private Subscription subscription;

            /**
             * 订阅建立时的回调方法
             * Publisher调用此方法来传递Subscription对象
             * @param s 订阅关系对象
             */
            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                System.out.println("订阅建立成功，开始请求数据...");

                // 背压控制：先请求2个数据
                // 这体现了订阅者可以控制数据接收的速度
                s.request(2);
            }

            /**
             * 接收数据的回调方法
             * 每当Publisher发送一个数据项时，此方法被调用
             * @param integer 接收到的数据
             */
            @Override
            public void onNext(Integer integer) {
                System.out.println("收到数据: " + integer);

                // 背压控制的体现：根据业务逻辑决定何时请求更多数据
                // 当收到数据"2"时，再请求3个数据
                if (integer == 2) {
                    System.out.println("收到数据2，继续请求3个数据...");
                    subscription.request(3);
                }
            }

            /**
             * 错误处理的回调方法
             * 当数据流中发生错误时，此方法被调用
             * @param t 发生的异常
             */
            @Override
            public void onError(Throwable t) {
                System.err.println("数据流发生错误:");
                t.printStackTrace();
            }

            /**
             * 数据流完成的回调方法
             * 当Publisher完成所有数据发送时，此方法被调用
             */
            @Override
            public void onComplete() {
                System.out.println("数据流完成，所有数据接收完毕！");
            }
        };

        // ========== 3. 建立订阅关系 ==========
        System.out.println("开始建立Publisher和Subscriber的订阅关系...");
        publisher.subscribe(subscriber);

        System.out.println("\n=== 执行流程说明 ===");
        System.out.println("1. Publisher.subscribe() 被调用");
        System.out.println("2. Publisher 创建 Subscription 并调用 Subscriber.onSubscribe()");
        System.out.println("3. Subscriber 在 onSubscribe() 中请求2个数据");
        System.out.println("4. Publisher 通过 Subscription.request() 发送数据");
        System.out.println("5. Subscriber 在 onNext() 中处理数据，并根据需要请求更多数据");
        System.out.println("6. 当发送完5个数据后，Publisher 调用 onComplete()");
    }
}