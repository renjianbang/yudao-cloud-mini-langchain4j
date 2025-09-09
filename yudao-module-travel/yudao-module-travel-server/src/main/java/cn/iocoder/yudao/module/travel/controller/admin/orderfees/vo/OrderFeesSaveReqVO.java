package cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单费用明细新增/修改 Request VO")
@Data
public class OrderFeesSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "116")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22452")
    @NotNull(message = "关联的订单ID不能为空")
    private Long orderId;

    @Schema(description = "关联的乘客ID (某些费用可能与特定乘客关联)", example = "24834")
    private Long passengerId;

    @Schema(description = "费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)不能为空")
    private String feeType;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "费用金额不能为空")
    private BigDecimal amount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "币种不能为空")
    private String currency;

    @Schema(description = "费用描述", example = "你猜")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最后更新时间不能为空")
    private LocalDateTime updatedAt;

}