package com.atguigu.study.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 19:22
 * @Description: TODO
 */
public interface ChatMemoryAssistant
{

    /**
     * 聊天带记忆缓存功能
     *
     * @param userId  用户 ID
     * @param prompt 消息
     * @return {@link String }
     */
    String chatWithChatMemory(@MemoryId Long userId, @UserMessage String prompt);
}
