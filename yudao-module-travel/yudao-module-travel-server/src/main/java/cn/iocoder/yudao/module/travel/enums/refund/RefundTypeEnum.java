package cn.iocoder.yudao.module.travel.enums.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退票类型枚举
 * 
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum RefundTypeEnum {

    VOLUNTARY(1, "自愿退票"),
    INVOLUNTARY(2, "非自愿退票");

    private final Integer type;
    private final String name;

    public static RefundTypeEnum valueOf(Integer type) {
        for (RefundTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}