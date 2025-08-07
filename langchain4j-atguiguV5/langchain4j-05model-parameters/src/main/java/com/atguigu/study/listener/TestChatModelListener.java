package com.atguigu.study.listener;

import cn.hutool.core.util.IdUtil;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-28 18:53
 * @Description: 知识出处，https://docs.langchain4j.dev/tutorials/spring-boot-integration#observability
 */
@Slf4j
public class TestChatModelListener implements ChatModelListener {
    @Override
    public void onRequest(ChatModelRequestContext requestContext) {

        // onRequest配置的k:v键值对，在onResponse阶段可以获得，上下文传递参数好用
        String uuidValue = IdUtil.simpleUUID();
        requestContext.attributes().put("TraceID", uuidValue);
        log.info("请求参数requestContext:{}", requestContext + "\t" + uuidValue);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        Object object = responseContext.attributes().get("TraceID");

        log.info("返回结果responseContext:{}", object);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        log.error("请求异常ChatModelErrorContext:{}", errorContext);
    }
}
