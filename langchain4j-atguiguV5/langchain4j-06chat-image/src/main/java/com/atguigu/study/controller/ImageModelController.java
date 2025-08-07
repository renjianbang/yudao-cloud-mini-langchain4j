package com.atguigu.study.controller;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 11:26
 * @Description: https://docs.langchain4j.dev/tutorials/chat-and-language-models/#multimodality
 */
@RestController
@Slf4j
public class ImageModelController
{
    @Autowired
    private ChatModel chatModel;

    @Value("classpath:static/images/mi.jpg")
    private Resource resource;//import org.springframework.core.io.Resource;

    /**
    * @Description: 通过Base64编码将图片转化为字符串
     *              结合ImageContent和TextContent形成UserMessage一起发送到模型进行处理。
    * @Auther: zzyybs@126.com
     *
     *测试地址：http://localhost:9006/image/call
    */
    @GetMapping(value = "/image/call")
    public String readImageContent() throws IOException
    {
        String result = null;

        //第一步，图片转码：通过Base64编码将图片转化为字符串
        byte[] byteArray = resource.getContentAsByteArray();
        String base64Data = Base64.getEncoder().encodeToString(byteArray);

        //第二步，提示词指定：结合ImageContent和TextContent一起发送到模型进行处理。
        UserMessage userMessage = UserMessage.from(
                TextContent.from("从下面图片种获取来源网站名称，股价走势和5月30号股价"),
                ImageContent.from(base64Data, "image/jpg")
        );
        //第三步，API调用：使用OpenAiChatModel来构建请求，并通过chat()方法调用模型。
        //请求内容包括文本提示和图片，模型会根据输入返回分析结果。
        ChatResponse chatResponse = chatModel.chat(userMessage);

        //第四步，解析与输出：从ChatResponse中获取AI大模型的回复，打印出处理后的结果。
        result = chatResponse.aiMessage().text();

        //后台打印
        System.out.println(result);

        //返回前台
        return result;
    }
}
