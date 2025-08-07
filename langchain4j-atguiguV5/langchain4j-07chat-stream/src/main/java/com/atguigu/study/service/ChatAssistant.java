package com.atguigu.study.service;

import reactor.core.publisher.Flux;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 16:19
 * @Description: TODO
 */
public interface ChatAssistant {
    String chat(String prompt);

    Flux<String> chatFlux(String prompt);
}
