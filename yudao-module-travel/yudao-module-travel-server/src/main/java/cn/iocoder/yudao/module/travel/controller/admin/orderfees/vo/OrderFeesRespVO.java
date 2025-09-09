package cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 订单费用明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderFeesRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "116")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22452")
    @ExcelProperty("关联的订单ID")
    private Long orderId;

    @Schema(description = "关联的乘客ID (某些费用可能与特定乘客关联)", example = "24834")
    @ExcelProperty("关联的乘客ID (某些费用可能与特定乘客关联)")
    private Long passengerId;

    @Schema(description = "费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)")
    private String feeType;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("费用金额")
    private BigDecimal amount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("币种")
    private String currency;

    @Schema(description = "费用描述", example = "你猜")
    @ExcelProperty("费用描述")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("最后更新时间")
    private LocalDateTime updatedAt;

}
