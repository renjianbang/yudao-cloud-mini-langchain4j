# LangChain4j 入门案例详解 - HelloWorld

## 📋 项目概述

`langchain4j-01helloworld` 是一个 LangChain4j 框架的入门案例，展示了如何使用 Spring Boot 集成 LangChain4j 并连接 MoonshotAI（Kimi）进行 AI 对话。

### 🎯 项目目标
- 学习 LangChain4j 基础用法
- 掌握 Spring Boot 与 LangChain4j 的集成
- 实现与 MoonshotAI 的对话功能
- 了解环境变量配置和依赖管理

## 🏗️ 项目结构

```
langchain4j-01helloworld/
├── src/main/java/com/atguigu/study/
│   ├── HelloLangChain4JApp.java          # 主启动类
│   ├── config/
│   │   ├── LLMConfig.java                # LangChain4j 配置类
│   │   └── MoonshotAiProperties.java     # MoonshotAI 属性配置
│   └── controller/
│       └── HelloLangChain4JController.java # REST 控制器
├── src/main/resources/
│   └── application.properties            # 应用配置
├── src/test/java/com/atguigu/study/
│   └── config/
│       └── MoonshotAiConfigTest.java     # 配置测试类
├── pom.xml                               # Maven 配置
└── README.md                             # 项目说明
```

## 🔧 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.2.0 | Web 框架 |
| LangChain4j | 0.34.0 | AI 集成框架 |
| Maven | 3.8.1+ | 构建工具 |
| MoonshotAI | - | AI 服务提供商 |

## 📦 核心依赖

```xml
<dependencies>
    <!-- Spring Boot Web Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- LangChain4j OpenAI Integration -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-open-ai</artifactId>
        <version>0.34.0</version>
    </dependency>
    
    <!-- LangChain4j Core -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j</artifactId>
        <version>0.34.0</version>
    </dependency>
</dependencies>
```

## 🚀 快速开始

### 1. 环境准备

#### 系统环境变量配置
在 Windows 系统环境变量中配置：

```bash
ANTHROPIC_API_KEY=sk-2VV6VAZSyjnD4y...  # 您的 MoonshotAI API 密钥
ANTHROPIC_BASE_URL=https://api.moonshot.cn/v1  # MoonshotAI API 基础URL
```

#### JDK 版本要求
- JDK 17 或更高版本
- Maven 3.8.1 或更高版本

### 2. 项目启动

#### 方式一：使用 Maven 命令
```bash
cd langchain4j-01helloworld
mvn spring-boot:run
```

#### 方式二：使用指定 JDK 17 的 Maven 命令
```bash
D:\Environment\JDK17\bin\java -cp "D:\Environment\apache-maven-3.8.1\boot\plexus-classworlds-2.6.0.jar" "-Dclassworlds.conf=D:\Environment\apache-maven-3.8.1\bin\m2.conf" "-Dmaven.home=D:\Environment\apache-maven-3.8.1" "-Dlibrary.jansi.path=D:\Environment\apache-maven-3.8.1\lib\jansi-native" "-Dmaven.multiModuleProjectDirectory=项目路径" org.codehaus.plexus.classworlds.launcher.Launcher spring-boot:run
```

### 3. 接口测试

启动成功后，访问以下URL进行测试：

```
http://localhost:9001/langchain4j/hello?question=你好，请介绍一下你自己
```

## 💻 核心代码解析

### 1. 主启动类 - HelloLangChain4JApp.java

```java
@SpringBootApplication
public class HelloLangChain4JApp {
    public static void main(String[] args) {
        SpringApplication.run(HelloLangChain4JApp.class, args);
    }
}
```

**说明**：标准的 Spring Boot 启动类，使用 `@SpringBootApplication` 注解启用自动配置。

### 2. LangChain4j 配置类 - LLMConfig.java

```java
@Configuration
@Slf4j
public class LLMConfig {
    
    @Autowired
    private MoonshotAiProperties moonshotAiProperties;
    
    @Bean
    @Primary
    public ChatLanguageModel moonshotAiChatModel() {
        // 验证配置
        moonshotAiProperties.validate();
        
        log.info("正在初始化 MoonshotAI 聊天模型...");
        
        return OpenAiChatModel.builder()
                .apiKey(moonshotAiProperties.getApiKey())
                .modelName(moonshotAiProperties.getModelName())
                .baseUrl(moonshotAiProperties.getBaseUrl())
                .timeout(Duration.ofSeconds(moonshotAiProperties.getTimeoutSeconds()))
                .maxRetries(moonshotAiProperties.getMaxRetries())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
```

**关键点**：
- 使用 `ChatLanguageModel` 接口（LangChain4j 0.34.0 版本）
- 通过 `OpenAiChatModel` 构建器模式创建模型实例
- 支持超时、重试等高级配置
- 启用请求/响应日志便于调试

### 3. 属性配置类 - MoonshotAiProperties.java

```java
@Data
@Component
@ConfigurationProperties(prefix = "moonshot")
public class MoonshotAiProperties {

    private String apiKey;
    private String baseUrl;
    private String modelName = "moonshot-v1-8k";
    private Integer timeoutSeconds = 60;
    private Integer maxRetries = 3;

    public String getApiKey() {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey;
        }
        return System.getenv("ANTHROPIC_API_KEY");
    }

    public String getBaseUrl() {
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            return baseUrl;
        }
        return System.getenv("ANTHROPIC_BASE_URL");
    }

    public void validate() {
        if (getApiKey() == null || getApiKey().trim().isEmpty()) {
            throw new IllegalStateException("MoonshotAI API Key is not configured.");
        }
        if (getBaseUrl() == null || getBaseUrl().trim().isEmpty()) {
            throw new IllegalStateException("MoonshotAI Base URL is not configured.");
        }
    }
}
```

**关键点**：
- 使用 `@ConfigurationProperties` 支持配置文件绑定
- 优先从环境变量读取敏感信息（API Key）
- 提供配置验证方法确保必要参数完整
- 支持默认值设置

### 4. REST 控制器 - HelloLangChain4JController.java

```java
@RestController
@Slf4j
public class HelloLangChain4JController {

    @Autowired
    @Qualifier("moonshotAiChatModel")
    private ChatLanguageModel chatModel;

    @GetMapping(value = "/langchain4j/hello")
    public String hello(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        try {
            log.info("收到用户问题: {}", question);

            String result = chatModel.generate(question);

            log.info("AI回复: {}", result);

            return result;
        } catch (Exception e) {
            log.error("调用AI模型失败", e);
            return "抱歉，AI服务暂时不可用，请稍后重试。错误信息: " + e.getMessage();
        }
    }
}
```

**关键点**：
- 使用 `@Qualifier` 明确指定注入的 Bean
- 调用 `generate()` 方法进行 AI 对话（LangChain4j 0.34.0 版本）
- 添加异常处理确保服务稳定性
- 记录请求和响应日志便于调试

## ⚙️ 配置文件详解

### application.properties

```properties
server.port=9001
spring.application.name=langchain4j-01helloworld

# MoonshotAI 配置
# 注意：实际的API密钥和URL从系统环境变量读取
# 系统环境变量：
# ANTHROPIC_API_KEY=sk-2VV6VAZSyjnD4y... (您的MoonshotAI API密钥)
# ANTHROPIC_BASE_URL=https://api.moonshot.cn/v1 (您的MoonshotAI API基础URL)

# MoonshotAI 可选配置（如果不设置，将使用默认值）
moonshot.model-name=moonshot-v1-8k
moonshot.timeout-seconds=60
moonshot.max-retries=3

# 临时覆盖环境变量进行测试（正式环境请修改系统环境变量）
moonshot.base-url=https://api.moonshot.cn/v1

# 日志配置
logging.level.dev.langchain4j=DEBUG
logging.level.com.atguigu.study=DEBUG
```

## 🧪 测试用例

### MoonshotAiConfigTest.java

```java
@SpringBootTest
@Slf4j
public class MoonshotAiConfigTest {

    @Autowired
    private MoonshotAiProperties moonshotAiProperties;

    @Autowired
    private ChatLanguageModel chatModel;

    @Test
    public void testMoonshotAiPropertiesConfiguration() {
        // 验证属性配置
        assertNotNull(moonshotAiProperties);
        assertEquals("moonshot-v1-8k", moonshotAiProperties.getModelName());
    }

    @Test
    public void testActualApiCall() {
        String question = "你好，请简单介绍一下你自己";
        String response = chatModel.generate(question);

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());

        log.info("问题: {}", question);
        log.info("回答: {}", response);
    }
}
```

## 🔍 关键技术点

### 1. LangChain4j 版本适配

**LangChain4j 0.34.0 版本的重要变化**：

| 旧版本 | 新版本 | 说明 |
|--------|--------|------|
| `ChatModel` | `ChatLanguageModel` | 接口名称变更 |
| `chat(String)` | `generate(String)` | 方法名称变更 |
| 简单配置 | 构建器模式 | 配置方式更加灵活 |

### 2. Maven JDK 版本问题解决

**问题现象**：
- Maven 使用 JDK 1.8，但项目需要 JDK 17
- 编译时出现 `--release` 标记无效错误

**解决方案**：
```bash
# 方法一：修改系统环境变量 JAVA_HOME
JAVA_HOME=D:\Environment\JDK17

# 方法二：使用完整 Java 路径运行 Maven
D:\Environment\JDK17\bin\java -cp "Maven路径" org.codehaus.plexus.classworlds.launcher.Launcher [命令]
```

### 3. 环境变量配置最佳实践

**安全性考虑**：
- API 密钥等敏感信息存储在环境变量中
- 配置文件中不包含真实的密钥信息
- 支持配置文件覆盖环境变量（便于测试）

**配置优先级**：
1. 配置文件中的显式配置
2. 系统环境变量
3. 默认值

## 📊 测试结果

### 成功案例

**请求**：
```
GET http://localhost:9001/langchain4j/hello?question=你好，请介绍一下你自己
```

**响应**：
```
你好！我是 Kimi，一个由人工智能技术驱动的助手。我擅长用中文和英文进行对话，可以帮助你解答各种问题，比如阅读和理解文件内容、提供信息查询服务、进行简单的数学计算等。我的目标是提供安全、有帮助且准确的回答。如果你有任何问题，随时可以问我，我会尽力帮助你。不过请注意，我会避免回答涉及恐怖主义、种族歧视、黄色暴力等不当内容的问题。
```

**日志输出**：
```
2025-08-06 收到用户问题: 你好，请介绍一下你自己
2025-08-06 正在初始化 MoonshotAI 聊天模型...
2025-08-06 Base URL: https://api.moonshot.cn/v1
2025-08-06 Model Name: moonshot-v1-8k
2025-08-06 AI回复: 你好！我是 Kimi...
```

## 🚨 常见问题与解决方案

### 1. ChatModel 类找不到

**错误信息**：
```
cannot find symbol: class ChatModel
```

**解决方案**：
- 将 `ChatModel` 更改为 `ChatLanguageModel`
- 更新 import 语句：`import dev.langchain4j.model.chat.ChatLanguageModel;`

### 2. chat() 方法不存在

**错误信息**：
```
cannot find symbol: method chat(java.lang.String)
```

**解决方案**：
- 将 `chat(question)` 更改为 `generate(question)`

### 3. API 调用失败

**错误信息**：
```
404 Not Found: /anthropic/chat/completions
```

**解决方案**：
- 检查 `ANTHROPIC_BASE_URL` 环境变量
- 确保 URL 为：`https://api.moonshot.cn/v1`（不是 `/anthropic`）

### 4. Maven 编译失败

**错误信息**：
```
invalid flag: --release
```

**解决方案**：
- 确保 Maven 使用 JDK 17
- 使用完整 Java 路径运行 Maven 命令

## 📚 学习要点总结

### 核心概念
1. **LangChain4j 框架**：Java 生态的 AI 应用开发框架
2. **ChatLanguageModel**：聊天语言模型的抽象接口
3. **OpenAiChatModel**：兼容 OpenAI API 协议的模型实现
4. **Spring Boot 集成**：通过配置类和依赖注入实现集成

### 最佳实践
1. **配置管理**：环境变量 + 配置文件的组合方式
2. **异常处理**：完善的错误处理和用户友好的错误信息
3. **日志记录**：详细的请求/响应日志便于调试
4. **测试驱动**：编写测试用例验证功能正确性

### 扩展方向
1. **多模型支持**：集成更多 AI 服务提供商
2. **流式响应**：支持实时流式对话
3. **上下文管理**：实现多轮对话的上下文保持
4. **功能增强**：添加文件上传、图片识别等功能

---

**文档版本**：v1.0
**创建时间**：2025-08-06
**作者**：renjianbang
**项目地址**：https://github.com/renjianbang/yudao-cloud-mini-langchain4j
```
