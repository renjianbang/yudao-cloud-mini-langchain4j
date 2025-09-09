package cn.iocoder.yudao.module.travel.dal.dataobject.rebooking;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 改签申请 DO
 *
 * @author 芋道源码
 */
@TableName("studio_rebooking_application")
@KeySequence("rebooking_application_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RebookingApplicationDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 关联的订单ID
     */
    private Long orderId;
    
    /**
     * 原航段ID
     */
    private Long originalSegmentId;
    
    /**
     * 乘客ID
     */
    private Long passengerId;
    
    /**
     * 新航班号
     */
    private String newFlightNo;
    
    /**
     * 新航司代码
     */
    private String newAirlineCode;
    
    /**
     * 新出发机场代码
     */
    private String newDepartureAirportCode;
    
    /**
     * 新到达机场代码
     */
    private String newArrivalAirportCode;
    
    /**
     * 新出发时间
     */
    private LocalDateTime newDepartureTime;
    
    /**
     * 新到达时间
     */
    private LocalDateTime newArrivalTime;
    
    /**
     * 新舱位等级
     */
    private String newCabinClass;
    
    /**
     * 改签费用
     */
    private BigDecimal changeFee;
    
    /**
     * 票价差额
     */
    private BigDecimal fareDifference;
    
    /**
     * 总费用
     */
    private BigDecimal totalFee;
    
    /**
     * 改签原因
     */
    private String reason;
    
    /**
     * 状态 (10: 待审核, 20: 已通过, 30: 已拒绝, 40: 已完成)
     */
    private Integer status;
    
    /**
     * 审核人
     */
    private String approver;
    
    /**
     * 审核时间
     */
    private LocalDateTime approveTime;
    
    /**
     * 备注
     */
    private String remarks;

}