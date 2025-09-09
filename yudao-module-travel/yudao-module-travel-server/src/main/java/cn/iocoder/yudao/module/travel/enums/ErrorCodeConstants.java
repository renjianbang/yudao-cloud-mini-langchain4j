package cn.iocoder.yudao.module.travel.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Travel 错误码枚举类
 *
 * travel 系统，使用 1-003-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 订单模块 1-003-001-000 ==========
    ErrorCode ORDER_NOT_EXISTS = new ErrorCode(1_003_001_000, "订单不存在");
    ErrorCode ORDER_STATUS_ERROR = new ErrorCode(1_003_001_001, "订单状态错误");

    // ========== 乘客模块 1-003-002-000 ==========
    ErrorCode PASSENGER_NOT_EXISTS = new ErrorCode(1_003_002_000, "乘客不存在");

    // ========== 航段模块 1-003-003-000 ==========
    ErrorCode FLIGHT_SEGMENT_NOT_EXISTS = new ErrorCode(1_003_003_000, "航段不存在");
    ErrorCode FLIGHT_SEGMENT_STATUS_ERROR = new ErrorCode(1_003_003_001, "航段状态错误");

    // ========== 改签模块 1-003-004-000 ==========
    ErrorCode REBOOKING_APPLICATION_NOT_EXISTS = new ErrorCode(1_003_004_000, "改签申请不存在");
    ErrorCode REBOOKING_APPLICATION_STATUS_ERROR = new ErrorCode(1_003_004_001, "改签申请状态错误，无法执行该操作");
    ErrorCode REBOOKING_FEE_CALCULATE_ERROR = new ErrorCode(1_003_004_002, "改签费用计算失败");
    ErrorCode REBOOKING_FLIGHT_NOT_AVAILABLE = new ErrorCode(1_003_004_003, "目标航班不可用");

    // ========== 出票模块 1-003-005-000 ==========
    ErrorCode TICKET_ISSUE_ERROR = new ErrorCode(1_003_005_000, "出票失败");
    ErrorCode TICKET_ALREADY_ISSUED = new ErrorCode(1_003_005_001, "票已出，无法重复出票");

    // ========== 退票模块 1-003-006-000 ==========
    ErrorCode REFUND_APPLICATION_NOT_EXISTS = new ErrorCode(1_003_006_000, "退票申请不存在");
    ErrorCode REFUND_ORDER_NOT_EXISTS = new ErrorCode(1_003_006_001, "订单不存在");
    ErrorCode REFUND_ORDER_STATUS_NOT_ALLOW = new ErrorCode(1_003_006_002, "订单状态不允许退票");
    ErrorCode REFUND_FLIGHT_ALREADY_DEPARTED = new ErrorCode(1_003_006_003, "航班已起飞，无法退票");
    ErrorCode REFUND_TYPE_INVALID = new ErrorCode(1_003_006_004, "退票类型无效");
    ErrorCode REFUND_STATUS_NOT_ALLOW_APPROVE = new ErrorCode(1_003_006_005, "退票申请状态不允许审核");
    ErrorCode REFUND_STATUS_NOT_ALLOW_EXECUTE = new ErrorCode(1_003_006_006, "退票申请状态不允许执行");
    ErrorCode REFUND_STATUS_NOT_ALLOW_CANCEL = new ErrorCode(1_003_006_007, "退票申请状态不允许取消");
    ErrorCode REFUND_POLICY_NOT_FOUND = new ErrorCode(1_003_006_008, "未找到适用的退票政策");
    ErrorCode REFUND_FEE_CALCULATE_FAILED = new ErrorCode(1_003_006_009, "退票费用计算失败");
    ErrorCode REFUND_SEGMENT_NOT_EXISTS = new ErrorCode(1_003_006_010, "航段不存在");
    ErrorCode REFUND_PASSENGER_NOT_EXISTS = new ErrorCode(1_003_006_011, "乘客不存在");
    ErrorCode REFUND_APPLICATION_ALREADY_EXISTS = new ErrorCode(1_003_006_012, "该乘客已有未完成的退票申请");

}