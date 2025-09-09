package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 退票申请创建 Request VO")
@Data
public class RefundApplicationCreateReqVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "航段ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "航段ID不能为空")
    private Long segmentId;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "航班号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "航班号不能为空")
    @Size(max = 10, message = "航班号长度不能超过10个字符")
    private String flightNo;

    @Schema(description = "航司代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "航司代码不能为空")
    private String airlineCode;

    @Schema(description = "出发机场代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "出发机场代码不能为空")
    private String departureAirportCode;

    @Schema(description = "到达机场代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "到达机场代码不能为空")
    private String arrivalAirportCode;

    @Schema(description = "出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出发时间不能为空")
    private LocalDateTime departureTime;

    @Schema(description = "到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "到达时间不能为空")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "舱位等级不能为空")
    private String cabinClass;

    @Schema(description = "原票价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原票价不能为空")
    @DecimalMin(value = "0", message = "原票价必须大于等于0")
    private BigDecimal originalTicketPrice;

    @Schema(description = "退票费", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票费不能为空")
    @DecimalMin(value = "0", message = "退票费必须大于等于0")
    private BigDecimal refundFee;

    @Schema(description = "实际退款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际退款金额不能为空")
    @DecimalMin(value = "0", message = "实际退款金额必须大于等于0")
    private BigDecimal actualRefundAmount;

    @Schema(description = "币种")
    private String currency = "CNY";

    @Schema(description = "退票原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "退票原因不能为空")
    @Size(max = 500, message = "退票原因长度不能超过500个字符")
    private String reason;

    @Schema(description = "退票类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票类型不能为空")
    @Min(value = 1, message = "退票类型必须是1或2")
    @Max(value = 2, message = "退票类型必须是1或2")
    private Integer refundType;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remarks;

}