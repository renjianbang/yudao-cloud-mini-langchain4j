package com.atguigu.langchain4j.config;

import com.atguigu.langchain4j.listener.TranslationServiceListener;
import com.atguigu.langchain4j.service.TranslationService;
//import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiServiceConfig {

    @Value("${openai.api.key:demo}")
    private String apiKey;

    @Value("${openai.api.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String modelName;

    @Value("${openai.api.temperature:0.7}")
    private Double temperature;

    @Value("${openai.api.max-tokens:1000}")
    private Integer maxTokens;

//    @Bean
//    public ChatLanguageModel chatLanguageModel() {
//        return OpenAiChatModel.builder()
//                .apiKey(apiKey)
//                .baseUrl(baseUrl)
//                .modelName(modelName)
//                .temperature(temperature)
//                .maxTokens(maxTokens)
//                .build();
//    }
//
//    @Bean
//    public TranslationService translationService(
//            ChatLanguageModel chatLanguageModel,
//            TranslationServiceListener translationServiceListener) {
//
//        return AiServices.builder(TranslationService.class)
//                .chatLanguageModel(chatLanguageModel)
//                .streamingChatLanguageModel(null)
//                .listener(translationServiceListener)
//                .build();
//    }
}