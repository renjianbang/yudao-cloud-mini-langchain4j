package cn.iocoder.yudao.module.travel.dal.dataobject.refund;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 退票政策 DO
 * 
 * @author 芋道源码
 */
@TableName("studio_refund_policy")
@KeySequence("studio_refund_policy_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundPolicyDO extends BaseDO {

    @TableId
    private Long id;
    
    /** 航司代码 */
    private String airlineCode;
    
    /** 舱位等级 */
    private String cabinClass;
    
    /** 退票类型 (1: 自愿, 2: 非自愿) */
    private Integer refundType;
    
    /** 退票费率 (0.0000-1.0000) */
    private BigDecimal feeRate;
    
    /** 最低退票费 */
    private BigDecimal minFee;
    
    /** 最高退票费 */
    private BigDecimal maxFee;
    
    /** 起飞前时间限制(小时) */
    private Integer timeLimitHours;
    
    /** 政策描述 */
    private String policyDesc;
    
    /** 生效日期 */
    private LocalDate effectiveDate;
    
    /** 失效日期 */
    private LocalDate expireDate;
    
    /** 状态 (0: 禁用, 1: 启用) */
    private Integer status;

}