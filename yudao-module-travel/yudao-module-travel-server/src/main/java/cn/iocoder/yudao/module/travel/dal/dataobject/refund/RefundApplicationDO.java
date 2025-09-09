package cn.iocoder.yudao.module.travel.dal.dataobject.refund;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退票申请 DO
 * 
 * @author 芋道源码
 */
@TableName("studio_refund_application")
@KeySequence("studio_refund_application_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundApplicationDO extends BaseDO {

    @TableId
    private Long id;
    
    /** 关联的订单ID */
    private Long orderId;
    
    /** 订单号 */
    private String orderNo;
    
    /** 航段ID */
    private Long segmentId;
    
    /** 乘客ID */
    private Long passengerId;
    
    /** 乘客姓名 */
    private String passengerName;
    
    // ==================== 航班信息 ====================
    
    /** 航班号 */
    private String flightNo;
    
    /** 航司代码 */
    private String airlineCode;
    
    /** 航司名称 */
    private String airlineName;
    
    /** 出发机场代码 */
    private String departureAirportCode;
    
    /** 出发机场名称 */
    private String departureAirportName;
    
    /** 到达机场代码 */
    private String arrivalAirportCode;
    
    /** 到达机场名称 */
    private String arrivalAirportName;
    
    /** 出发时间 */
    private LocalDateTime departureTime;
    
    /** 到达时间 */
    private LocalDateTime arrivalTime;
    
    /** 舱位等级 */
    private String cabinClass;
    
    // ==================== 费用信息 ====================
    
    /** 原票价 */
    private BigDecimal originalTicketPrice;
    
    /** 退票费 */
    private BigDecimal refundFee;
    
    /** 退票金额 */
    private BigDecimal refundAmount;
    
    /** 实际退款金额 */
    private BigDecimal actualRefundAmount;
    
    /** 币种 */
    private String currency;
    
    // ==================== 业务信息 ====================
    
    /** 退票原因 */
    private String reason;
    
    /** 退票类型 (1: 自愿退票, 2: 非自愿退票) */
    private Integer refundType;
    
    /** 状态 (10: 待审核, 20: 已通过, 30: 已拒绝, 40: 已完成, 50: 已取消) */
    private Integer status;
    
    // ==================== 审核信息 ====================
    
    /** 审核人 */
    private String approver;
    
    /** 审核时间 */
    private LocalDateTime approveTime;
    
    /** 审核备注 */
    private String approveRemarks;
    
    /** 申请备注 */
    private String remarks;

}