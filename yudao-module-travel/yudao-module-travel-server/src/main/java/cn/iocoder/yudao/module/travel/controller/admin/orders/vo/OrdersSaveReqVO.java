package cn.iocoder.yudao.module.travel.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单新增/修改 Request VO")
@Data
public class OrdersSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "下单用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @NotNull(message = "下单用户ID不能为空")
    private Long userId;

    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "币种不能为空")
    private String currency;

    @Schema(description = "订单状态 (10: 待支付, 20: 已支付, 30: 已出票, 40: 已完成, 90: 已取消)", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "订单状态不能为空")
    private Integer orderStatus;

    @Schema(description = "支付状态 (10: 未支付, 20: 已支付, 30: 部分退款, 40: 全额退款)", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "支付状态不能为空")
    private Integer paymentStatus;

    @Schema(description = "预订类型 (ONLINE: 在线预订, OFFLINE: 线下预订)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "预订类型不能为空")
    private String bookingType;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系人姓名不能为空")
    private String contactName;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系人电话不能为空")
    private String contactPhone;

    @Schema(description = "联系人邮箱")
    private String contactEmail;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最后更新时间不能为空")
    private LocalDateTime updatedAt;

}