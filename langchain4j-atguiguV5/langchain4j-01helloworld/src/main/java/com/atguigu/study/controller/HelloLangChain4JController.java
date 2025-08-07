package com.atguigu.study.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-27 21:43
 * @Description: LangChain4j Hello World 控制器
 * 使用 MoonshotAI 进行对话
 */
@RestController
@Slf4j
public class HelloLangChain4JController {

    /**
     * 注入 MoonshotAI 聊天模型
     * 使用系统环境变量配置：
     * - ANTHROPIC_API_KEY: MoonshotAI API密钥
     * - ANTHROPIC_BASE_URL: MoonshotAI API基础URL
     */
    @Autowired
    @Qualifier("moonshotAiChatModel")
    private ChatLanguageModel chatModel;

    /**
     * Hello World 接口
     * 访问地址: http://localhost:9001/langchain4j/hello?question=如何学习java
     *
     * @param question 用户问题，默认为"你是谁"
     * @return AI回复内容
     */
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
