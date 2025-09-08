# LangChain4J 消息类型与ChatPrompt模块详解

## 1. 消息类型概述

LangChain4J提供了多种消息类型来构建完整的对话流程：

| 消息类型 | 用途 | 示例 |
|---------|------|------|
| UserMessage | 用户输入 | `UserMessage.from("你好")` |
| AiMessage | AI回复 | `AiMessage.from("你好！")` |
| SystemMessage | 系统指令 | `SystemMessage.from("你是一个翻译助手")` |
| ToolExecutionResultMessage | 工具执行结果 | `ToolExecutionResultMessage.from("calc", "42")` |
| CustomMessage | 自定义消息 | `CustomMessage.from("type", "data")` |

## 2. ChatPrompt核心功能

### 2.1 基础构建
```java
ChatPrompt prompt = ChatPrompt.builder()
    .addSystemMessage("你是一个翻译助手")
    .addUserMessage("请翻译：Hello")
    .build();
```

### 2.2 完整对话流程
```java
ChatPrompt conversation = ChatPrompt.builder()
    .addSystemMessage("你是一个Java专家")
    .addUserMessage("如何实现单例模式？")
    .addAiMessage("""
        public class Singleton {
            private static Singleton instance;
            private Singleton() {}
            public static Singleton getInstance() {
                if (instance == null) {
                    instance = new Singleton();
                }
                return instance;
            }
        }
        """)
    .build();
```

## 3. 高级用法

### 3.1 工具调用集成
```java
// 工具调用请求
AiMessage toolRequest = AiMessage.from(
    new ToolUse("calculator", 
        "{\"operation\":\"multiply\",\"numbers\":[6,7]}"));

// 工具执行结果    
ToolExecutionResultMessage toolResult = 
    ToolExecutionResultMessage.from("calculator", "42");

ChatPrompt toolPrompt = ChatPrompt.builder()
    .addSystemMessage("你可以使用计算器工具")
    .addUserMessage("计算6乘以7")
    .add(toolRequest)
    .add(toolResult)
    .build();
```

### 3.2 自定义消息处理
```java
// 自定义消息处理器
class CustomMessageHandler implements MessageHandler {
    @Override
    public void handle(CustomMessage message) {
        System.out.println("处理自定义消息: " + message.content());
    }
}

// 注册处理器
ChatPrompt customPrompt = ChatPrompt.builder()
    .add(CustomMessage.from("alert", "重要通知"))
    .withMessageHandler(new CustomMessageHandler())
    .build();
```

## 4. 最佳实践

1. **上下文管理**：使用SystemMessage设置明确的角色和任务
2. **工具调用**：规范工具请求和结果的JSON格式
3. **错误处理**：对工具调用失败添加fallback响应
4. **性能优化**：对大文本消息进行分块处理

## 5. 完整示例项目

建议在langchain4j-09chat-prompt模块中创建以下示例：

1. **基础对话示例**：演示多轮对话构建
2. **工具集成示例**：展示完整工具调用流程
3. **自定义扩展示例**：实现自定义消息处理

## 6. 常见问题

**Q: 如何持久化对话历史？**
A: 使用ChatMemory持久化实现，如：
```java
ChatMemory memory = PersistentChatMemory.builder()
    .id("conversation1")
    .store(new JdbcChatMemoryStore(dataSource))
    .build();
```

**Q: 消息顺序是否影响对话质量？**
A: 是的，保持正确的User-AI消息交替顺序非常重要