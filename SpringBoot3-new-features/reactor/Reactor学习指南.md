# Reactor学习指南

## 📚 学习概述

本指南将带你深入学习Project Reactor，这是Spring生态系统中的响应式编程库。通过系统化的示例和实践，你将掌握Reactor的核心概念和实际应用。

## 🎯 学习目标

- 理解Reactor的核心概念：Mono和Flux
- 掌握各种操作符的使用
- 学会异步编程和线程调度
- 掌握错误处理和重试机制
- 理解背压控制的重要性
- 能够在实际项目中应用Reactor

## 📖 学习路径

### 1. 基础概念 (ReactorBasics.java)

**学习内容：**
- Mono和Flux的基本概念
- 创建Publisher的各种方式
- 订阅和消费数据
- 基本操作符使用

**核心知识点：**
```java
// Mono - 0或1个元素
Mono<String> mono = Mono.just("Hello");

// Flux - 0到N个元素  
Flux<Integer> flux = Flux.range(1, 5);

// 订阅方式
flux.subscribe(
    value -> System.out.println(value),    // onNext
    error -> System.err.println(error),    // onError
    () -> System.out.println("Complete")   // onComplete
);
```

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.basic.ReactorBasics
```

### 2. 操作符学习 (ReactorOperators.java)

**学习内容：**
- 转换操作符：map, flatMap, cast, index
- 过滤操作符：filter, distinct, take, skip
- 组合操作符：merge, zip, concat
- 聚合操作符：reduce, collect, count
- 条件操作符：all, any, hasElements

**核心知识点：**
```java
// 转换操作
flux.map(x -> x * 2)           // 一对一转换
    .filter(x -> x > 5)        // 过滤
    .take(3)                   // 取前3个
    .collectList()             // 收集为列表
```

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.operators.ReactorOperators
```

### 3. 异步和调度器 (ReactorSchedulers.java)

**学习内容：**
- 不同类型的Schedulers
- subscribeOn vs publishOn
- 异步操作的线程切换
- 并行处理
- 延迟和定时操作

**核心知识点：**
```java
// 调度器类型
Schedulers.immediate()      // 当前线程
Schedulers.single()         // 单线程
Schedulers.boundedElastic() // 弹性线程池(I/O)
Schedulers.parallel()       // 并行线程池(CPU)

// 线程切换
flux.subscribeOn(Schedulers.boundedElastic())  // 影响整个链
    .publishOn(Schedulers.parallel())          // 只影响下游
```

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.async.ReactorSchedulers
```

### 4. 错误处理 (ReactorErrorHandling.java)

**学习内容：**
- 基本错误处理：onErrorReturn, onErrorResume
- 错误继续：onErrorContinue
- 重试机制：retry, retryWhen
- 错误映射：onErrorMap
- 错误处理最佳实践

**核心知识点：**
```java
// 错误处理策略
flux.onErrorReturn("默认值")                    // 返回默认值
    .onErrorResume(error -> Flux.just("备用"))  // 切换到备用流
    .onErrorContinue((error, item) -> {         // 跳过错误继续
        log.warn("跳过: {}", item);
    })
    .retry(3)                                   // 重试3次
```

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.error.ReactorErrorHandling
```

### 5. 背压控制 (ReactorBackpressure.java)

**学习内容：**
- 背压的概念和问题
- 背压处理策略：buffer, drop, latest, error
- 手动背压控制
- 限流操作：limitRate
- 实际应用场景

**核心知识点：**
```java
// 背压策略
flux.onBackpressureBuffer(100)     // 缓冲策略
    .onBackpressureDrop()           // 丢弃策略
    .onBackpressureLatest()         // 保留最新
    .limitRate(10)                  // 限制速率
```

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.backpressure.ReactorBackpressure
```

### 6. 实际应用 (ReactorRealWorldExample.java)

**学习内容：**
- 电商订单处理系统
- 异步服务调用
- 错误处理和重试
- 并行处理
- 背压控制
- 实时监控和统计

**核心特性：**
- ✅ 异步非阻塞处理
- ✅ 错误处理和重试
- ✅ 背压控制
- ✅ 并行处理
- ✅ 超时控制
- ✅ 资源管理

**运行方式：**
```bash
java cn.iocoder.cloud.reactor.application.ReactorRealWorldExample
```

## 🔧 环境要求

- JDK 17+
- Maven 3.6+
- Project Reactor 3.6.0

## 🚀 快速开始

1. **克隆项目并进入reactor模块**
   ```bash
   cd SpringBoot3-new-features/reactor
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行基础示例**
   ```bash
   mvn exec:java -Dexec.mainClass="cn.iocoder.cloud.reactor.basic.ReactorBasics"
   ```

4. **按顺序学习各个示例**
   - 基础概念 → 操作符 → 异步调度 → 错误处理 → 背压控制 → 实际应用

## 💡 学习建议

### 初学者路径
1. 先理解响应式编程的基本概念
2. 从ReactorBasics开始，掌握Mono和Flux
3. 学习常用操作符
4. 理解异步和线程调度
5. 掌握错误处理
6. 学习背压控制
7. 通过实际应用巩固知识

### 进阶学习
1. 深入理解Reactor的内部机制
2. 学习自定义操作符
3. 掌握性能调优技巧
4. 学习与Spring WebFlux集成
5. 了解测试最佳实践

## 📊 核心概念对比

| 概念 | 传统编程 | Reactor |
|------|----------|---------|
| 数据处理 | 同步阻塞 | 异步非阻塞 |
| 错误处理 | try-catch | onError操作符 |
| 集合操作 | Stream API | Flux操作符 |
| 并发控制 | Thread/ExecutorService | Schedulers |
| 背压处理 | 无内置支持 | 内置背压控制 |

## 🎯 最佳实践

1. **选择合适的Publisher**
   - 单个值或空值：使用Mono
   - 多个值：使用Flux

2. **合理使用调度器**
   - I/O操作：boundedElastic()
   - CPU密集型：parallel()
   - 简单操作：immediate()

3. **错误处理策略**
   - 提供默认值：onErrorReturn
   - 切换备用流：onErrorResume
   - 跳过错误：onErrorContinue
   - 重试：retry/retryWhen

4. **背压控制**
   - 根据系统能力设置缓冲区大小
   - 使用limitRate控制处理速率
   - 选择合适的背压策略

5. **性能优化**
   - 避免阻塞操作
   - 合理使用并行处理
   - 及时释放资源
   - 使用适当的缓冲策略

## 🔍 常见问题

**Q: 什么时候使用Mono，什么时候使用Flux？**
A: Mono用于0或1个元素的场景（如HTTP请求响应），Flux用于0到N个元素的场景（如数据流处理）。

**Q: subscribeOn和publishOn有什么区别？**
A: subscribeOn影响整个操作链的执行线程，publishOn只影响下游操作的执行线程。

**Q: 如何处理背压问题？**
A: 可以使用onBackpressureBuffer、onBackpressureDrop、onBackpressureLatest等策略，或使用limitRate限制速率。

**Q: 错误处理的最佳实践是什么？**
A: 根据业务需求选择合适的错误处理策略，提供有意义的错误信息，合理使用重试机制。

## 📚 参考资料

- [Project Reactor官方文档](https://projectreactor.io/docs)
- [Reactor Core参考指南](https://projectreactor.io/docs/core/release/reference/)
- [Spring WebFlux文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Reactive Streams规范](https://www.reactive-streams.org/)

---

**祝你学习愉快！🚀**

如果在学习过程中遇到问题，建议：
1. 仔细阅读示例代码和注释
2. 运行示例观察输出结果
3. 尝试修改参数观察变化
4. 查阅官方文档获取更多信息
