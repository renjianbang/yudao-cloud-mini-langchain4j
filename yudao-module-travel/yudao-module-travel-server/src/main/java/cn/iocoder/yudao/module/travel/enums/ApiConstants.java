package cn.iocoder.yudao.module.travel.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Travel 模块 API 常量
 *
 * @author 芋道源码
 */
public interface ApiConstants {

    /**
     * 服务名
     *
     * 注意，需要保证和 spring.application.name 保持一致
     */
    String NAME = "travel-server";

    String PREFIX = "/travel-api";

    String VERSION = "1.0.0";

}