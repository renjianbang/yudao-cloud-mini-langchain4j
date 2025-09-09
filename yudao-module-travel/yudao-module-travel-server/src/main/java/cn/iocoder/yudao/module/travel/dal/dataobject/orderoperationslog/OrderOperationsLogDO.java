package cn.iocoder.yudao.module.travel.dal.dataobject.orderoperationslog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单操作日志 DO
 *
 * @author 芋道源码
 */
@TableName("studio_order_operations_log")
@KeySequence("studio_order_operations_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOperationsLogDO extends BaseDO {

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
     * 操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)
     */
    private String operationType;
    /**
     * 操作前的数据快照 (JSON格式)
     */
    private String contentBefore;
    /**
     * 操作后的数据快照 (JSON格式)
     */
    private String contentAfter;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * 操作备注
     */
    private String remark;
    /**
     * 操作时间
     */
    private LocalDateTime createdAt;


}
