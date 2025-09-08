package com.atguigu.study.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-27 22:04
 * @Description: LangChain4j配置类，支持多种AI模型
 * 知识出处 https://docs.langchain4j.dev/get-started
 */
@Configuration
@Slf4j
public class LLMConfig {

    @Autowired
    private MoonshotAiProperties moonshotAiProperties;

    /**
     * MoonshotAI 聊天模型配置
     * 从系统环境变量读取配置信息：
     * - ANTHROPIC_API_KEY: MoonshotAI API密钥
     * - ANTHROPIC_BASE_URL: MoonshotAI API基础URL
     */
    @Bean
    @Primary
    //@Primary 注解用于标记某个 Bean 为“首选 Bean”。当 Spring 容器中存在多个同类型 Bean 时，自动注入（如 @Autowired）会优先选择带有 @Primary 的 Bean。这样可以避免因类型冲突导致的注入错误。
    public ChatModel moonshotAiChatModel() {
        // 验证配置
        moonshotAiProperties.validate();

        log.info("正在初始化 MoonshotAI 聊天模型...");
        log.info("Base URL: {}", moonshotAiProperties.getBaseUrl());
        log.info("Model Name: {}", moonshotAiProperties.getModelName());

        // 使用建造者模式构建 OpenAiChatModel 实例
        // 虽然名为 OpenAiChatModel，但由于 OpenAI 兼容协议的广泛采用，
        // 可以用于连接多种大模型服务（如 MoonshotAI、通义千问、智谱AI等）
        return OpenAiChatModel.builder()
                // 设置 API 密钥，用于身份验证和授权访问
                // 从配置文件中读取，确保敏感信息不硬编码在代码中
                .apiKey(moonshotAiProperties.getApiKey())
                // 指定要使用的具体模型名称
                // 例如: "moonshot-v1-8k", "gpt-3.5-turbo", "qwen-plus" 等
                .modelName(moonshotAiProperties.getModelName())
                // 设置 API 服务的基础 URL 地址
                // 不同服务商有不同的端点，如 MoonshotAI: "https://api.moonshot.cn/v1"
                .baseUrl(moonshotAiProperties.getBaseUrl())
                // 配置网络请求超时时间，防止长时间等待
                // 将秒数转换为 Duration 对象，建议设置为 30-120 秒
                .timeout(Duration.ofSeconds(moonshotAiProperties.getTimeoutSeconds()))
                // 设置失败重试次数，提高服务的可靠性
                // 当网络波动或服务暂时不可用时，会自动重试指定次数
                .maxRetries(moonshotAiProperties.getMaxRetries())
                // 启用请求日志记录，便于调试和监控
                // 会记录发送给 AI 模型的完整请求内容（注意可能包含敏感信息）
                .logRequests(true)
                // 启用响应日志记录，便于分析 AI 模型的返回结果
                // 会记录 AI 模型返回的完整响应内容，有助于问题排查
                .logResponses(true)
                // 构建并返回配置完成的 ChatModel 实例
                // 该实例将被 Spring 容器管理，可在其他地方通过依赖注入使用
                .build();
    }

    /**
     * 阿里云通义千问配置（备用）
     * 如果需要使用阿里云通义千问，可以取消注释并配置相应环境变量
     */
    /*
    @Bean("qwenChatModel")
    public ChatModel qwenChatModel() {
        String qwenApiKey = System.getenv("QWEN_API_KEY");
        if (qwenApiKey == null || qwenApiKey.trim().isEmpty()) {
            throw new IllegalStateException("QWEN_API_KEY environment variable is not set");
        }

        return OpenAiChatModel.builder()
                .apiKey(qwenApiKey)
                .modelName("qwen-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .timeout(Duration.ofSeconds(60))
                .maxRetries(3)
                .build();
    }
    */

}
