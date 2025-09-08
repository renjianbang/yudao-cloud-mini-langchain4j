package com.atguigu.langchain4j.service;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.V;

public interface TranslationService {

    @SystemMessage("你是一个专业的翻译助手，请将用户提供的文本翻译成指定的语言。")
    String translate(@UserMessage String text, @V("targetLanguage") String targetLanguage);

    @SystemMessage("你是一个专业的翻译助手，请将用户提供的文本翻译成中文。")
    String translateToChinese(@UserMessage String text);

    @SystemMessage("你是一个专业的翻译助手，请将用户提供的文本翻译成英文。")
    String translateToEnglish(@UserMessage String text);
}