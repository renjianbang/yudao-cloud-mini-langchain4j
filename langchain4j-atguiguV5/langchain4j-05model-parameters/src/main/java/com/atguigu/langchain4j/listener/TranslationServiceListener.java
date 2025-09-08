package com.atguigu.langchain4j.listener;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.listener.AiServiceTokenStreamListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TranslationServiceListener implements AiServiceTokenStreamListener {

    @Override
    public void onToken(String token, TokenStream tokenStream) {
        log.info("收到翻译结果片段: {}", token);
    }

    @Override
    public void onComplete(TokenStream tokenStream) {
        log.info("翻译完成，总token数: {}", tokenStream.totalTokenCount());
    }

    @Override
    public void onError(Throwable error, TokenStream tokenStream) {
        log.error("翻译过程中发生错误: {}", error.getMessage(), error);
    }
}