# Reactive Stream 学习指南

## 📚 目录

1. [为什么有Reactive Stream规范](#1-为什么有reactive-stream规范)
2. [Reactive Stream规范核心接口](#2-reactive-stream规范核心接口)
3. [Reactive Stream发布数据](#3-reactive-stream发布数据)
4. [Reactive Stream发布订阅写法](#4-reactive-stream发布订阅写法)
5. [Reactive Stream四大核心组件](#5-reactive-stream四大核心组件)
6. [Project Reactor实际应用](#6-project-reactor实际应用)
7. [综合应用示例](#7-综合应用示例)
8. [学习总结](#8-学习总结)

---

## 1. 为什么有Reactive Stream规范

### 1.1 传统同步编程的问题

传统的同步编程模型在处理大量数据或高并发场景时存在以下问题：

- **阻塞式I/O操作**：会导致线程等待，资源利用率低
- **背压(Backpressure)问题**：生产者产生数据的速度超过消费者处理的速度
- **内存溢出**：无法控制数据流的速度，可能导致内存耗尽
- **线程池耗尽**：大量阻塞操作占用线程资源

### 1.2 Reactive Stream的解决方案

Reactive Stream规范解决了这些问题：

- ✅ **异步非阻塞处理**：提高系统响应性
- ✅ **背压控制机制**：消费者可以控制数据接收速度
- ✅ **流式数据处理**：内存占用可控
- ✅ **更好的资源利用率**：高效利用系统资源

### 1.3 适用场景

- 高并发系统
- 大数据流处理
- 实时数据处理
- 微服务架构中的异步通信
- IoT数据处理

**示例代码位置**：`cn.iocoder.cloud.reactivestream.basic.ReactiveStreamIntroduction`

---

## 2. Reactive Stream规范核心接口

Reactive Stream规范定义了4个核心接口：

### 2.1 Publisher<T> - 发布者

```java
public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}
```

**职责**：
- 提供数据源，可以发布0到N个数据项
- 可以发送错误信号或完成信号
- 支持多个订阅者订阅

### 2.2 Subscriber<T> - 订阅者

```java
public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T item);
    void onError(Throwable throwable);
    void onComplete();
}
```

**方法说明**：
- `onSubscribe()`: 订阅成功时调用，接收Subscription对象
- `onNext()`: 接收数据项时调用
- `onError()`: 发生错误时调用
- `onComplete()`: 数据流完成时调用

### 2.3 Subscription - 订阅关系

```java
public interface Subscription {
    void request(long n);
    void cancel();
}
```

**方法说明**：
- `request(n)`: 请求n个数据项（背压控制的核心）
- `cancel()`: 取消订阅，停止数据流

### 2.4 Processor<T,R> - 处理器

```java
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
    // 继承了Subscriber和Publisher的所有方法
}
```

**特点**：
- 作为Subscriber接收上游数据
- 作为Publisher向下游发送处理后的数据
- 可以进行数据转换、过滤、聚合等操作

**示例代码位置**：`cn.iocoder.cloud.reactivestream.interfaces.CoreInterfaces`

---

## 3. Reactive Stream发布数据

### 3.1 简单Publisher实现

创建一个基本的Publisher需要实现以下功能：

1. **数据源管理**：维护要发布的数据
2. **订阅处理**：处理订阅者的订阅请求
3. **背压控制**：根据订阅者请求发送数据
4. **生命周期管理**：处理完成和错误状态

### 3.2 核心实现要点

```java
public class SimplePublisher implements Publisher<Integer> {
    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
        // 创建订阅关系
        SimpleSubscription subscription = new SimpleSubscription(subscriber, data);
        subscriber.onSubscribe(subscription);
    }
}
```

### 3.3 背压控制实现

```java
@Override
public void request(long n) {
    if (n <= 0) {
        subscriber.onError(new IllegalArgumentException("请求数量必须大于0"));
        return;
    }
    
    long previousRequested = requested.getAndAdd(n);
    if (previousRequested == 0) {
        sendData(); // 开始发送数据
    }
}
```

**示例代码位置**：`cn.iocoder.cloud.reactivestream.publisher.SimplePublisher`

---

## 4. Reactive Stream发布订阅写法

### 4.1 完整的发布订阅模式

发布订阅模式包含以下组件：

1. **Publisher**：数据发布者
2. **Subscriber**：数据订阅者
3. **异步处理**：非阻塞数据处理
4. **错误处理**：统一的异常处理机制

### 4.2 实际应用示例

```java
// 创建发布者
NewsPublisher publisher = new NewsPublisher(newsData);

// 创建订阅者
NewsSubscriber subscriber = new NewsSubscriber("订阅者A");

// 建立订阅关系
publisher.subscribe(subscriber);
```

### 4.3 背压控制策略

- **请求控制**：订阅者通过`request(n)`控制数据接收速度
- **缓冲策略**：使用缓冲区处理速度不匹配问题
- **取消机制**：支持取消订阅停止数据流

**示例代码位置**：`cn.iocoder.cloud.reactivestream.pubsub.PublishSubscribeExample`

---

## 5. Reactive Stream四大核心组件

### 5.1 组件协作关系

```
Publisher -> [Processor] -> Subscriber
     ↑                          ↓
     └── Subscription ←---------┘
```

### 5.2 数据流模型

1. **订阅阶段**：Subscriber订阅Publisher
2. **建立连接**：Publisher调用Subscriber.onSubscribe()
3. **数据传输**：通过onNext()传输数据
4. **流结束**：通过onComplete()或onError()结束

### 5.3 核心规则

- **背压规则**：Publisher不能发送超过Subscriber请求数量的数据
- **线程安全规则**：Subscriber的方法调用必须是串行的
- **终止规则**：onError()或onComplete()调用后，流必须终止
- **订阅规则**：每个Subscription只能被一个Subscriber使用

**示例代码位置**：`cn.iocoder.cloud.reactivestream.components.FourCoreComponents`

---

## 6. Project Reactor实际应用

### 6.1 核心类型

- **Mono<T>**：表示0或1个元素的异步序列
- **Flux<T>**：表示0到N个元素的异步序列

### 6.2 基本操作

```java
// Mono示例
Mono<String> mono = Mono.just("Hello Reactive")
    .map(String::toUpperCase)
    .onErrorReturn("默认值");

// Flux示例
Flux<Integer> flux = Flux.range(1, 5)
    .filter(i -> i % 2 == 0)
    .map(i -> i * 2);
```

### 6.3 高级特性

- **背压控制**：自动处理生产者和消费者速度不匹配
- **异步处理**：使用Schedulers进行线程调度
- **组合操作**：merge、zip、concat等操作符
- **错误处理**：onErrorReturn、onErrorContinue等

**示例代码位置**：`cn.iocoder.cloud.reactivestream.reactor.ProjectReactorExample`

---

## 7. 综合应用示例

### 7.1 电商订单处理系统

模拟了一个完整的电商订单处理流程：

1. **订单创建**：定时创建订单
2. **订单验证**：异步验证订单信息
3. **支付处理**：处理支付逻辑
4. **发货处理**：处理发货流程
5. **统计分析**：实时统计订单数据
6. **监控告警**：监控异常订单

### 7.2 核心特性展示

- ✅ **异步非阻塞**：所有操作都是异步的
- ✅ **背压控制**：控制并发处理数量
- ✅ **错误处理**：优雅处理各种异常
- ✅ **流式处理**：实时处理订单流
- ✅ **组合操作**：灵活组合处理逻辑

**示例代码位置**：`cn.iocoder.cloud.reactivestream.application.ReactiveStreamApplication`

---

## 8. 学习总结

### 8.1 Reactive Stream的核心价值

1. **性能提升**：异步非阻塞处理提高系统吞吐量
2. **资源优化**：更好的资源利用率，减少线程占用
3. **背压控制**：防止系统过载，保证系统稳定性
4. **错误处理**：统一的错误处理机制
5. **组合性**：丰富的操作符支持复杂的数据处理逻辑

### 8.2 适用场景

- **高并发系统**：处理大量并发请求
- **实时数据处理**：流式数据处理
- **微服务架构**：服务间异步通信
- **大数据处理**：处理大量数据流
- **IoT应用**：处理设备数据流

### 8.3 最佳实践

1. **合理使用背压**：根据系统能力控制数据流速度
2. **错误处理**：为每个操作提供适当的错误处理
3. **资源管理**：及时释放资源，避免内存泄漏
4. **线程调度**：合理使用Schedulers进行线程调度
5. **测试验证**：充分测试异步逻辑和错误场景

### 8.4 学习路径建议

1. **理解概念**：掌握Reactive Stream的核心概念
2. **实践基础**：从简单的Publisher/Subscriber开始
3. **学习框架**：掌握Project Reactor或RxJava
4. **实际应用**：在项目中应用Reactive编程
5. **性能优化**：学习性能调优和监控

---

## 🎯 快速开始

1. **运行基础示例**：
   ```bash
   java cn.iocoder.cloud.reactivestream.basic.ReactiveStreamIntroduction
   ```

2. **学习核心接口**：
   ```bash
   java cn.iocoder.cloud.reactivestream.interfaces.CoreInterfaces
   ```

3. **体验发布订阅**：
   ```bash
   java cn.iocoder.cloud.reactivestream.pubsub.PublishSubscribeExample
   ```

4. **运行综合示例**：
   ```bash
   java cn.iocoder.cloud.reactivestream.application.ReactiveStreamApplication
   ```

---

## 📖 参考资料

- [Reactive Streams规范](https://www.reactive-streams.org/)
- [Project Reactor文档](https://projectreactor.io/docs)
- [RxJava文档](https://github.com/ReactiveX/RxJava)
- [Spring WebFlux文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)

---

**祝你学习愉快！🚀**
