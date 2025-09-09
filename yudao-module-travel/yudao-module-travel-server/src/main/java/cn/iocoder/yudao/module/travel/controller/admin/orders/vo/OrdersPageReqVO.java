package cn.iocoder.yudao.module.travel.controller.admin.orders.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单分页 Request VO")
@Data
public class OrdersPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "下单用户ID", example = "12345")
    private Long userId;

    @Schema(description = "订单状态 (10: 待支付, 20: 已支付, 30: 已出票, 40: 已完成, 90: 已取消)", example = "10")
    private Integer orderStatus;

    @Schema(description = "支付状态 (10: 未支付, 20: 已支付, 30: 部分退款, 40: 全额退款)", example = "10")
    private Integer paymentStatus;

    @Schema(description = "预订类型 (ONLINE: 在线预订, OFFLINE: 线下预订)")
    private String bookingType;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人电话")
    private String contactPhone;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}