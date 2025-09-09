package cn.iocoder.yudao.module.travel.enums.rebooking;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 改签状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum RebookingStatusEnum {

    PENDING(10, "待审核"),
    APPROVED(20, "已通过"),
    REJECTED(30, "已拒绝"),
    COMPLETED(40, "已完成");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

}