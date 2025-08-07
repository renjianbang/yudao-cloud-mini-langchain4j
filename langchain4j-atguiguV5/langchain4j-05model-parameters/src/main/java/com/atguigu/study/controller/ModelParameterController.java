package com.atguigu.study.controller;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-28 18:33
 * @Description: 知识出处，https://docs.langchain4j.dev/tutorials/model-parameters/
 */
@RestController
@Slf4j
public class ModelParameterController {
    @Resource
    private ChatModel chatModelQwen;

    // http://localhost:9005/modelparam/config
    @GetMapping(value = "/modelparam/config")
    public String config(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt) {
        String result = chatModelQwen.chat(prompt);

        System.out.println("通过langchain4j调用模型返回结果：" + result);

        return result;
    }
}
