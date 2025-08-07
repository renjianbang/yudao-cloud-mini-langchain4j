package com.atguigu.study.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 18:59
 * @Description: TODO
 */
public interface ChatAssistant
{
    /**
    * @Description: 普通聊天对话，不带记忆缓存功能
    */
    String chat(String prompt);
}
