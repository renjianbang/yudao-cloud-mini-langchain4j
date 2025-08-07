package com.atguigu.study.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MoonshotAI 配置测试类
 * 
 * @author renjianbang
 * @date 2025-08-06
 */
@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
    "moonshot.model-name=moonshot-v1-8k",
    "moonshot.timeout-seconds=30",
    "moonshot.max-retries=2"
})
public class MoonshotAiConfigTest {
    
    @Autowired
    private MoonshotAiProperties moonshotAiProperties;
    
    @Autowired
    private ChatLanguageModel chatModel;
    
    @Test
    public void testMoonshotAiPropertiesConfiguration() {
        log.info("测试 MoonshotAI 属性配置...");
        
        // 验证属性配置
        assertNotNull(moonshotAiProperties);
        assertEquals("moonshot-v1-8k", moonshotAiProperties.getModelName());
        assertEquals(30, moonshotAiProperties.getTimeoutSeconds());
        assertEquals(2, moonshotAiProperties.getMaxRetries());
        
        log.info("MoonshotAI 属性配置测试通过");
    }
    
    @Test
    public void testEnvironmentVariables() {
        log.info("测试环境变量配置...");
        
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        String baseUrl = System.getenv("ANTHROPIC_BASE_URL");
        
        log.info("API Key 是否配置: {}", apiKey != null && !apiKey.trim().isEmpty());
        log.info("Base URL 是否配置: {}", baseUrl != null && !baseUrl.trim().isEmpty());
        
        if (apiKey != null && baseUrl != null) {
            log.info("环境变量配置完整，可以进行实际API调用测试");
            
            // 如果环境变量配置完整，验证配置不会抛出异常
            assertDoesNotThrow(() -> moonshotAiProperties.validate());
        } else {
            log.warn("环境变量未完整配置，跳过实际API调用测试");
        }
    }
    
    @Test
    public void testChatModelBean() {
        log.info("测试 ChatModel Bean 注入...");
        
        assertNotNull(chatModel);
        log.info("ChatModel Bean 注入成功: {}", chatModel.getClass().getSimpleName());
    }
    
    /**
     * 实际API调用测试（需要配置环境变量）
     * 只有在环境变量配置完整时才会执行
     */
    @Test
    public void testActualApiCall() {
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        String baseUrl = System.getenv("ANTHROPIC_BASE_URL");
        
        if (apiKey == null || baseUrl == null || apiKey.trim().isEmpty() || baseUrl.trim().isEmpty()) {
            log.info("跳过实际API调用测试：环境变量未配置");
            return;
        }
        
        try {
            log.info("开始实际API调用测试...");
            
            String question = "你好，请简单介绍一下你自己";
            String response = chatModel.generate(question);
            
            assertNotNull(response);
            assertFalse(response.trim().isEmpty());
            
            log.info("API调用成功！");
            log.info("问题: {}", question);
            log.info("回答: {}", response);
            
        } catch (Exception e) {
            log.error("API调用失败", e);
            fail("API调用失败: " + e.getMessage());
        }
    }
}
