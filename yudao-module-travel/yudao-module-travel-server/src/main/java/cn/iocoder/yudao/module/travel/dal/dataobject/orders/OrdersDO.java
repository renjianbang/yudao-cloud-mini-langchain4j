package cn.iocoder.yudao.module.travel.dal.dataobject.orders;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单 DO
 *
 * @author 芋道源码
 */
@TableName("studio_orders")
@KeySequence("studio_orders_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 下单用户ID
     */
    private Long userId;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 订单状态 (10: 待支付, 20: 已支付, 30: 已出票, 40: 已完成, 90: 已取消)
     */
    private Integer orderStatus;
    /**
     * 支付状态 (10: 未支付, 20: 已支付, 30: 部分退款, 40: 全额退款)
     */
    private Integer paymentStatus;
    /**
     * 预订类型 (ONLINE: 在线预订, OFFLINE: 线下预订)
     */
    private String bookingType;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 联系人邮箱
     */
    private String contactEmail;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;

}