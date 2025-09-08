package com.atguigu.langchain4j;

import com.atguigu.langchain4j.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TranslationServiceTest {

    @Autowired
    private TranslationService translationService;

    @Test
    void testTranslateToEnglish() {
        String text = "你好，世界！这是一个测试。";
        String result = translationService.translateToEnglish(text);
        System.out.println("中文到英文翻译结果: " + result);
    }

    @Test
    void testTranslateToChinese() {
        String text = "Hello, world! This is a test.";
        String result = translationService.translateToChinese(text);
        System.out.println("英文到中文翻译结果: " + result);
    }

    @Test
    void testTranslate() {
        String text = "我爱编程和人工智能";
        String result = translationService.translate(text, "日文");
        System.out.println("中文到日文翻译结果: " + result);
    }
}