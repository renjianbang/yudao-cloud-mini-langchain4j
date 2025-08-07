package com.atguigu.study.service;

import com.atguigu.study.entities.LawPrompt;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 21:24
 * @Description: TODO
 */
public interface LawAssistant
{
    //案例1 @SystemMessage+@UserMessage+@V
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")

    @UserMessage("请回答以下法律问题：{{question}},字数控制在{{length}}以内")

    String chat(@V("question") String question, @V("length") int length);


    //案例2 新建带着@StructuredPrompt的业务实体类，比如LawPrompt
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    String chat(LawPrompt lawPrompt);
}
