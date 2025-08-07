package com.atguigu.study.service;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-28 17:28
 * @Description: 我们知道，按照Java开发一般习惯，有接口就要有实现类
 * 比如接口ChatAssistant，就会有实现类ChatAssistantImpl
 * 现在用高阶api-AIServics不用你自己写impl实现类，交给langchain4j给你搞定
 *
 * 本次配置用的是langchain4j原生整合，没有引入sprinboot，不需要接口头上配置@AiService注解标签
 */
public interface ChatAssistant
{
    String chat(String prompt);
}
