package cn.iocoder.yudao.module.travel.dal.dataobject.orderfees;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单费用明细 DO
 *
 * @author 芋道源码
 */
@TableName("studio_order_fees")
@KeySequence("studio_order_fees_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFeesDO extends BaseDO {

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
     * 关联的乘客ID (某些费用可能与特定乘客关联)
     */
    private Long passengerId;
    /**
     * 费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)
     */
    private String feeType;
    /**
     * 费用金额
     */
    private BigDecimal amount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 费用描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;


}
