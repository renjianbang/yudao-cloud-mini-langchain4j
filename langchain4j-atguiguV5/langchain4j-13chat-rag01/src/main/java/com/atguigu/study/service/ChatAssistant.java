package com.atguigu.study.service;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-02 21:01
 * @Description: TODO
 */
public interface ChatAssistant {

    /**
     * 聊天
     *
     * @param message 消息
     * @return {@link String }
     */
    String chat(String message);
}
