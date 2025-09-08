package com.atguigu.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-02 16:07
 * @Description: 将客户和大模型的对话问答保存进Redis进行持久化记忆留存
 * 知识出处,https://docs.langchain4j.dev/tutorials/chat-memory#persistence
 */
@SpringBootApplication
public class ChatPersistenceLangChain4JApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatPersistenceLangChain4JApp.class, args);
    }
}
