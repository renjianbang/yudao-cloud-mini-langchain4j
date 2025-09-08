package com.atguigu.study.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-02 16:10
 * @Description: TODO
 */

public interface ChatPersistenceAssistant {
    /**
     * 聊天
     *
     * @param userId  用户 ID
     * @param message 消息
     * @return {@link String }
     */
    String chat(@MemoryId Long userId, @UserMessage String message);
}

