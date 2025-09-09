package cn.iocoder.yudao.module.travel.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrdersRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "下单用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @ExcelProperty("下单用户ID")
    private Long userId;

    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("币种")
    private String currency;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("订单状态")
    private Integer orderStatus;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("支付状态")
    private Integer paymentStatus;

    @Schema(description = "预订类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("预订类型")
    private String bookingType;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系人姓名")
    private String contactName;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系人电话")
    private String contactPhone;

    @Schema(description = "联系人邮箱")
    @ExcelProperty("联系人邮箱")
    private String contactEmail;

    @Schema(description = "订单备注")
    @ExcelProperty("订单备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}