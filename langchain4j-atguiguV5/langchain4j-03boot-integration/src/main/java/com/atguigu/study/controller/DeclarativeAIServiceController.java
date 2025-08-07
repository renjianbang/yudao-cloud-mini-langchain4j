package com.atguigu.study.controller;

import com.atguigu.study.service.ChatAssistant;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-18 15:43
 * @Description: TODO
 */
@RestController
public class DeclarativeAIServiceController {
    @Resource
    private ChatAssistant chatAssistantQwen;

    // http://localhost:9003/lc4j/boot/declarative
    @GetMapping(value = "/lc4j/boot/declarative")
    public String declarative(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt) {
        return chatAssistantQwen.chat(prompt);
    }
}
