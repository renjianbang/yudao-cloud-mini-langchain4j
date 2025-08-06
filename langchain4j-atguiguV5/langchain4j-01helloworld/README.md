# LangChain4j + MoonshotAI 集成示例

这是一个使用 LangChain4j 集成 MoonshotAI 的 Spring Boot 示例项目。

## 🚀 快速开始

### 1. 环境变量配置

在系统环境变量中配置以下信息：

```bash
# Windows 系统环境变量
ANTHROPIC_API_KEY=sk-2VV6VAZSyjnD4y...  # 您的 MoonshotAI API 密钥
ANTHROPIC_BASE_URL=https://api.moonshot.cn/v1  # MoonshotAI API 基础URL
```

**注意**: 请将 `sk-2VV6VAZSyjnD4y...` 替换为您实际的 MoonshotAI API 密钥。

### 2. 启动应用

```bash
cd langchain4j-atguiguV5/langchain4j-01helloworld
mvn spring-boot:run
```

### 3. 测试接口

访问以下URL进行测试：

```
http://localhost:9001/langchain4j/hello?question=你好，请介绍一下你自己
```

## 📁 项目结构

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
└── src/test/java/com/atguigu/study/
    └── config/
        └── MoonshotAiConfigTest.java     # 配置测试类
```

## ⚙️ 配置说明

### 环境变量

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `ANTHROPIC_API_KEY` | MoonshotAI API 密钥 | `sk-2VV6VAZSyjnD4y...` |
| `ANTHROPIC_BASE_URL` | MoonshotAI API 基础URL | `https://api.moonshot.cn/v1` |

### 应用配置

在 `application.properties` 中可以配置以下可选参数：

```properties
# MoonshotAI 模型配置
moonshot.model-name=moonshot-v1-8k      # 模型名称
moonshot.timeout-seconds=60             # 请求超时时间
moonshot.max-retries=3                  # 最大重试次数

# 日志配置
logging.level.dev.langchain4j=DEBUG     # LangChain4j 日志级别
logging.level.com.atguigu.study=DEBUG   # 应用日志级别
```

## 🧪 测试

运行测试用例：

```bash
mvn test
```

测试包括：
- 配置属性验证
- 环境变量检查
- Bean 注入测试
- 实际 API 调用测试（需要配置环境变量）

## 📝 API 接口

### GET /langchain4j/hello

**描述**: 与 MoonshotAI 进行对话

**参数**:
- `question` (可选): 用户问题，默认为"你是谁"

**示例**:
```bash
curl "http://localhost:9001/langchain4j/hello?question=如何学习Java"
```

**响应**:
```
学习Java可以按照以下步骤进行：

1. 基础语法学习
2. 面向对象编程
3. 集合框架
4. 多线程编程
5. Spring框架
...
```

## 🔧 故障排除

### 1. 环境变量未配置

**错误**: `ANTHROPIC_API_KEY environment variable is not set`

**解决**: 确保在系统环境变量中正确配置了 `ANTHROPIC_API_KEY` 和 `ANTHROPIC_BASE_URL`

### 2. API 调用失败

**错误**: `AI服务暂时不可用`

**解决**: 
- 检查网络连接
- 验证 API 密钥是否正确
- 确认 API 基础URL 是否正确

### 3. 启动失败

**错误**: Bean 创建失败

**解决**: 
- 检查环境变量配置
- 查看应用日志获取详细错误信息

## 📚 相关文档

- [LangChain4j 官方文档](https://docs.langchain4j.dev/)
- [MoonshotAI API 文档](https://platform.moonshot.cn/docs)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
