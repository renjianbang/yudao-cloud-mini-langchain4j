# LangChain4j 模型参数配置与@AiService案例

本项目演示了如何使用LangChain4j的@AiService注解创建AI服务，并配置模型参数和监听器。

## 功能特性

- 使用@AiService注解创建翻译服务
- 配置OpenAI模型参数（温度、最大token数等）
- 实现AI服务监听器，监控请求和响应
- 提供REST API接口进行翻译
- 支持中英文互译及多语言翻译

## 项目结构

```
├── src/main/java/com/atguigu/langchain4j/
│   ├── ModelParametersApplication.java     # 主启动类
│   ├── config/
│   │   └── AiServiceConfig.java           # AI服务配置类
│   ├── controller/
│   │   └── TranslationController.java     # REST控制器
│   ├── service/
│   │   └── TranslationService.java        # AI服务接口
│   └── listener/
│       └── TranslationServiceListener.java # 监听器实现
├── src/main/resources/
│   └── application.yml                    # 配置文件
└── src/test/java/
    └── TranslationServiceTest.java        # 测试类
```

## 配置说明

### 1. OpenAI API配置
在`application.yml`中配置：
```yaml
openai:
  api:
    key: your-openai-api-key-here
    base-url: https://api.openai.com/v1
    model: gpt-3.5-turbo
    temperature: 0.7
    max-tokens: 1000
```

### 2. 模型参数说明
- **temperature**: 控制输出的随机性（0-1，值越大越随机）
- **max-tokens**: 限制生成内容的最大token数
- **model**: 指定使用的OpenAI模型

## 使用方式

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. API接口

#### 通用翻译接口
```bash
curl -X POST "http://localhost:8085/api/translation/translate?text=你好世界&targetLanguage=英文"
```

#### 翻译为中文
```bash
curl -X POST "http://localhost:8085/api/translation/translate-to-chinese" \
  -H "Content-Type: text/plain" \
  -d "Hello World"
```

#### 翻译为英文
```bash
curl -X POST "http://localhost:8085/api/translation/translate-to-english" \
  -H "Content-Type: text/plain" \
  -d "你好世界"
```

### 3. 运行测试
```bash
mvn test
```

## 监听器功能

TranslationServiceListener实现了以下监控功能：
- 记录每个token的生成过程
- 统计总token数
- 捕获并记录错误信息

## 注意事项

1. 需要有效的OpenAI API密钥
2. 确保网络可以访问OpenAI API
3. 根据实际需求调整模型参数
4. 生产环境中建议使用环境变量存储API密钥

## 扩展建议

- 添加缓存机制减少重复翻译
- 实现用户限流功能
- 支持更多翻译提供商
- 添加翻译质量评估功能