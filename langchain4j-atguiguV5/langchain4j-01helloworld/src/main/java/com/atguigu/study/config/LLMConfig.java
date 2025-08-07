package com.atguigu.study.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
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
    public ChatLanguageModel moonshotAiChatModel() {
        // 验证配置
        moonshotAiProperties.validate();

        log.info("正在初始化 MoonshotAI 聊天模型...");
        log.info("Base URL: {}", moonshotAiProperties.getBaseUrl());
        log.info("Model Name: {}", moonshotAiProperties.getModelName());

        return OpenAiChatModel.builder()
                .apiKey(moonshotAiProperties.getApiKey())
                .modelName(moonshotAiProperties.getModelName())
                .baseUrl(moonshotAiProperties.getBaseUrl())
                .timeout(Duration.ofSeconds(moonshotAiProperties.getTimeoutSeconds()))
                .maxRetries(moonshotAiProperties.getMaxRetries())
                .logRequests(true)  // 启用请求日志
                .logResponses(true) // 启用响应日志
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
