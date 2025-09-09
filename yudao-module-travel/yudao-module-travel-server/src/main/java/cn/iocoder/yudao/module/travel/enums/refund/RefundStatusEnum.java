package cn.iocoder.yudao.module.travel.enums.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退票状态枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {

    PENDING(10, "待审核"),
    APPROVED(20, "已通过"), 
    REJECTED(30, "已拒绝"),
    COMPLETED(40, "已完成"),
    CANCELLED(50, "已取消");

    private final Integer status;
    private final String name;

    public static RefundStatusEnum valueOf(Integer status) {
        for (RefundStatusEnum value : values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }

}