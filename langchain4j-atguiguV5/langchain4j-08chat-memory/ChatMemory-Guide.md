# LangChain4j 记忆模块详解

## 1. 记忆模块概述

LangChain4j的记忆模块(Chat Memory)用于在对话过程中保持上下文信息，使AI能够记住之前的对话内容。这是构建连贯对话系统的关键组件。

## 2. 核心概念

### 2.1 记忆类型
- **短期记忆**：保存当前对话的上下文
- **长期记忆**：保存历史对话的关键信息

### 2.2 记忆存储方式
- **内存存储**：默认方式，适合简单应用
- **持久化存储**：使用数据库存储对话历史
- **向量存储**：将对话内容向量化后存储

## 3. 主要实现类

### 3.1 MessageWindowChatMemory
```java
// 创建固定窗口大小的记忆
ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
```

### 3.2 PersistentChatMemory
```java
// 使用数据库持久化记忆
ChatMemory chatMemory = PersistentChatMemory.builder()
    .id("user123")
    .store(new JdbcChatMemoryStore(dataSource))
    .build();
```

## 4. 使用示例

### 4.1 基本用法
```java
// 初始化记忆
ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(5);

// 添加消息
chatMemory.add(new HumanMessage("你好"));
chatMemory.add(new AiMessage("你好！有什么我可以帮助你的吗？"));

// 获取记忆
List<ChatMessage> messages = chatMemory.messages();
```

### 4.2 与AI服务集成
```java
AiServices<TranslationService> aiServices = AiServices.builder(TranslationService.class)
    .chatLanguageModel(chatLanguageModel)
    .chatMemory(chatMemory) // 注入记忆
    .build();
```

## 5. 高级配置

### 5.1 自定义记忆策略
```java
ChatMemory customMemory = new CustomChatMemory()
    .withMessageFilter(message -> {
        // 自定义消息过滤逻辑
        return !message.text().contains("敏感词");
    });
```

### 5.2 记忆压缩
```java
// 使用摘要压缩长对话
chatMemory.withCompressor(new SummaryMemoryCompressor(llm));
```

## 6. 最佳实践

1. 根据对话长度选择合适的窗口大小
2. 对敏感信息实现自定义过滤
3. 长期对话建议使用持久化存储
4. 考虑实现记忆摘要功能减少token消耗

## 7. 常见问题

### Q: 记忆窗口设置多大合适？
A: 一般5-10条消息，需平衡上下文长度和token消耗

### Q: 如何实现跨会话记忆？
A: 使用PersistentChatMemory并持久化到数据库

## 8. 参考资源

- [LangChain4j官方文档](https://docs.langchain4j.dev)
- [记忆模块API文档](https://javadoc.io/doc/dev.langchain4j/langchain4j-core/latest/dev/langchain4j/memory/ChatMemory.html)