package cn.iocoder.cloud.reactivestream.interfaces;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.Processor;

/**
 * Reactive Stream 规范核心接口详解
 * 
 * Reactive Stream规范定义了4个核心接口：
 * 1. Publisher<T>：发布者，数据源
 * 2. Subscriber<T>：订阅者，数据消费者
 * 3. Subscription：订阅关系，控制数据流
 * 4. Processor<T,R>：处理器，既是发布者又是订阅者
 * 
 * @author iocoder
 */
public class CoreInterfaces {
    
    /**
     * 1. Publisher<T> 发布者接口
     * 
     * 职责：
     * - 提供数据源
     * - 接受订阅者的订阅
     * - 向订阅者发送数据、错误或完成信号
     */
    public static void explainPublisher() {
        System.out.println("=== Publisher<T> 发布者接口 ===");
        System.out.println("接口定义：");
        System.out.println("public interface Publisher<T> {");
        System.out.println("    void subscribe(Subscriber<? super T> subscriber);");
        System.out.println("}");
        System.out.println();
        System.out.println("职责：");
        System.out.println("- 数据源，可以发布0到N个数据项");
        System.out.println("- 可以发送错误信号");
        System.out.println("- 可以发送完成信号");
        System.out.println("- 支持多个订阅者订阅");
        System.out.println();
    }
    
    /**
     * 2. Subscriber<T> 订阅者接口
     * 
     * 职责：
     * - 订阅发布者
     * - 接收数据、错误或完成信号
     * - 通过Subscription控制数据流
     */
    public static void explainSubscriber() {
        System.out.println("=== Subscriber<T> 订阅者接口 ===");
        System.out.println("接口定义：");
        System.out.println("public interface Subscriber<T> {");
        System.out.println("    void onSubscribe(Subscription subscription);");
        System.out.println("    void onNext(T item);");
        System.out.println("    void onError(Throwable throwable);");
        System.out.println("    void onComplete();");
        System.out.println("}");
        System.out.println();
        System.out.println("方法说明：");
        System.out.println("- onSubscribe(): 订阅成功时调用，接收Subscription对象");
        System.out.println("- onNext(): 接收数据项时调用");
        System.out.println("- onError(): 发生错误时调用");
        System.out.println("- onComplete(): 数据流完成时调用");
        System.out.println();
    }
    
    /**
     * 3. Subscription 订阅关系接口
     * 
     * 职责：
     * - 控制数据流的速度（背压控制）
     * - 取消订阅
     */
    public static void explainSubscription() {
        System.out.println("=== Subscription 订阅关系接口 ===");
        System.out.println("接口定义：");
        System.out.println("public interface Subscription {");
        System.out.println("    void request(long n);");
        System.out.println("    void cancel();");
        System.out.println("}");
        System.out.println();
        System.out.println("方法说明：");
        System.out.println("- request(n): 请求n个数据项（背压控制的核心）");
        System.out.println("- cancel(): 取消订阅，停止数据流");
        System.out.println();
        System.out.println("背压控制原理：");
        System.out.println("- 订阅者通过request(n)告诉发布者需要多少数据");
        System.out.println("- 发布者不会发送超过请求数量的数据");
        System.out.println("- 这样可以防止快速生产者压垮慢速消费者");
        System.out.println();
    }
    
    /**
     * 4. Processor<T,R> 处理器接口
     * 
     * 职责：
     * - 既是订阅者又是发布者
     * - 可以对数据进行转换处理
     */
    public static void explainProcessor() {
        System.out.println("=== Processor<T,R> 处理器接口 ===");
        System.out.println("接口定义：");
        System.out.println("public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {");
        System.out.println("    // 继承了Subscriber和Publisher的所有方法");
        System.out.println("}");
        System.out.println();
        System.out.println("特点：");
        System.out.println("- 作为Subscriber接收上游数据");
        System.out.println("- 作为Publisher向下游发送处理后的数据");
        System.out.println("- 可以进行数据转换、过滤、聚合等操作");
        System.out.println("- 支持链式操作");
        System.out.println();
    }
    
    /**
     * Reactive Stream的数据流模型
     */
    public static void explainDataFlowModel() {
        System.out.println("=== Reactive Stream 数据流模型 ===");
        System.out.println();
        System.out.println("数据流向：");
        System.out.println("Publisher -> [Processor] -> Subscriber");
        System.out.println();
        System.out.println("信号类型：");
        System.out.println("1. onNext信号：携带数据项");
        System.out.println("2. onError信号：携带错误信息，终止流");
        System.out.println("3. onComplete信号：表示流正常结束");
        System.out.println();
        System.out.println("流的生命周期：");
        System.out.println("1. 订阅阶段：Subscriber订阅Publisher");
        System.out.println("2. 建立连接：Publisher调用Subscriber.onSubscribe()");
        System.out.println("3. 数据传输：通过onNext()传输数据");
        System.out.println("4. 流结束：通过onComplete()或onError()结束");
        System.out.println();
    }
    
    /**
     * Reactive Stream规范的核心规则
     */
    public static void explainCoreRules() {
        System.out.println("=== Reactive Stream 核心规则 ===");
        System.out.println();
        System.out.println("1. 背压规则：");
        System.out.println("   - Publisher不能发送超过Subscriber请求数量的数据");
        System.out.println("   - Subscriber必须通过request()方法请求数据");
        System.out.println();
        System.out.println("2. 线程安全规则：");
        System.out.println("   - Subscriber的方法调用必须是串行的");
        System.out.println("   - 不能并发调用onNext()、onError()、onComplete()");
        System.out.println();
        System.out.println("3. 终止规则：");
        System.out.println("   - onError()或onComplete()调用后，流必须终止");
        System.out.println("   - 终止后不能再发送任何信号");
        System.out.println();
        System.out.println("4. 订阅规则：");
        System.out.println("   - 每个Subscription只能被一个Subscriber使用");
        System.out.println("   - Subscriber可以订阅多个Publisher");
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("Reactive Stream 核心接口详解");
        System.out.println("==============================\n");
        
        explainPublisher();
        explainSubscriber();
        explainSubscription();
        explainProcessor();
        explainDataFlowModel();
        explainCoreRules();
    }
}
