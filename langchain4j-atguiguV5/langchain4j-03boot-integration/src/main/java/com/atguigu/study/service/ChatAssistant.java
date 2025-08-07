package com.atguigu.study.service;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import org.springframework.stereotype.Service;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-18 15:40
 * @Description:
 * 知识出处，https://docs.langchain4j.dev/tutorials/spring-boot-integration/#spring-boot-starter-for-declarative-ai-services
 */
@AiService
public interface ChatAssistant {
    String chat(String prompt);
}
