package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 改签申请创建请求 VO")
@Data
public class RebookingApplicationCreateReqVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "原航段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "原航段ID不能为空")
    private Long originalSegmentId;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "新航班号", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU585")
    @NotBlank(message = "新航班号不能为空")
    private String newFlightNo;

    @Schema(description = "新航司代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU")
    @NotBlank(message = "新航司代码不能为空")
    private String newAirlineCode;

    @Schema(description = "新出发机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PVG")
    @NotBlank(message = "新出发机场代码不能为空")
    private String newDepartureAirportCode;

    @Schema(description = "新到达机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "LAX")
    @NotBlank(message = "新到达机场代码不能为空")
    private String newArrivalAirportCode;

    @Schema(description = "新出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "新出发时间不能为空")
    private LocalDateTime newDepartureTime;

    @Schema(description = "新到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "新到达时间不能为空")
    private LocalDateTime newArrivalTime;

    @Schema(description = "新舱位等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "BUSINESS")
    @NotBlank(message = "新舱位等级不能为空")
    private String newCabinClass;

    @Schema(description = "改签费用", example = "200.00")
    private BigDecimal changeFee;

    @Schema(description = "票价差额", example = "500.00")
    private BigDecimal fareDifference;

    @Schema(description = "总费用", example = "750.00")
    private BigDecimal totalFee;

    @Schema(description = "改签原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间变更")
    @NotBlank(message = "改签原因不能为空")
    private String reason;

    @Schema(description = "备注", example = "紧急情况需要改签")
    private String remarks;

}