# LangChain4J 持久化功能详解

## 1. 概述
LangChain4J的持久化功能允许将会话历史保存到数据库或文件中，实现对话状态的长期保存和恢复。

## 2. 核心组件
- `ChatMemoryStore`: 持久化存储接口
- `PersistentChatMemory`: 持久化聊天记忆实现
- `DatabaseChatMemoryStore`: 数据库存储实现
- `FileChatMemoryStore`: 文件存储实现

## 3. 配置步骤
### 3.1 数据库持久化
```java
@Bean
public ChatMemoryStore chatMemoryStore(DataSource dataSource) {
    return new DatabaseChatMemoryStore(dataSource);
}
```

### 3.2 文件持久化
```java
@Bean
public ChatMemoryStore chatMemoryStore() {
    return new FileChatMemoryStore(Paths.get("./chat-memory"));
}
```

## 4. 代码示例
### 4.1 使用持久化聊天记忆
```java
@AiService
public interface ChatService {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}
```

### 4.2 恢复会话历史
```java
List<ChatMessage> history = chatMemoryStore.getMessages(sessionId);
```

## 5. 最佳实践
1. 为每个用户/会话使用唯一的`MemoryId`
2. 定期清理过期的会话数据
3. 考虑使用Redis等高性能存储

## 6. 常见问题
### Q: 如何选择存储方式？
A: 根据数据量和性能需求选择：
- 小规模: 文件存储
- 大规模: 数据库存储

### Q: 数据如何加密？
A: 可在存储层实现加密逻辑，或使用加密文件系统。