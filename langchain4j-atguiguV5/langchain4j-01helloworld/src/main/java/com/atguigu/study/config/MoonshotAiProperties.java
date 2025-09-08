package com.atguigu.study.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MoonshotAI 配置属性类
 * 从系统环境变量读取配置信息
 * 
 * @author renjianbang
 * @date 2025-08-06
 */
@Data
@Component
//@ConfigurationProperties(prefix = "moonshot") 的作用是将配置文件（如 application.properties 或 application.yml）中以 moonshot 开头的属性自动绑定到该类的字段上
@ConfigurationProperties(prefix = "moonshot")
public class MoonshotAiProperties {

    /**
     * API密钥，从环境变量 ANTHROPIC_API_KEY 读取
     */
    private String apiKey;

    /**
     * API基础URL，从环境变量 ANTHROPIC_BASE_URL 读取
     */
    private String baseUrl;

    /**
     * 默认模型名称
     */
//    private String modelName = "moonshot-v1-8k";
    private String modelName = "kimi-k2-0711-preview";

    /**
     * 请求超时时间（秒）
     */
    private Integer timeoutSeconds = 60;

    /**
     * 最大重试次数
     */
    private Integer maxRetries = 3;

    /**
     * 获取API密钥，优先从环境变量读取
     */
    public String getApiKey() {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey;
        }
        return System.getenv("ANTHROPIC_API_KEY");
    }

    /**
     * 获取基础URL，优先从环境变量读取
     */
    public String getBaseUrl() {
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            return baseUrl;
        }
        return System.getenv("ANTHROPIC_BASE_URL");
    }

    /**
     * 验证配置是否完整
     */
    public void validate() {
        if (getApiKey() == null || getApiKey().trim().isEmpty()) {
            throw new IllegalStateException("MoonshotAI API Key is not configured. Please set ANTHROPIC_API_KEY environment variable.");
        }
        if (getBaseUrl() == null || getBaseUrl().trim().isEmpty()) {
            throw new IllegalStateException("MoonshotAI Base URL is not configured. Please set ANTHROPIC_BASE_URL environment variable.");
        }
    }
}
