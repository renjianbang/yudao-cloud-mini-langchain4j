package com.atguigu.langchain4j.controller;

import com.atguigu.langchain4j.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translation")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping("/translate")
    public String translate(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "英文") String targetLanguage) {
        return translationService.translate(text, targetLanguage);
    }

    @PostMapping("/translate-to-chinese")
    public String translateToChinese(@RequestBody String text) {
        return translationService.translateToChinese(text);
    }

    @PostMapping("/translate-to-english")
    public String translateToEnglish(@RequestBody String text) {
        return translationService.translateToEnglish(text);
    }
}