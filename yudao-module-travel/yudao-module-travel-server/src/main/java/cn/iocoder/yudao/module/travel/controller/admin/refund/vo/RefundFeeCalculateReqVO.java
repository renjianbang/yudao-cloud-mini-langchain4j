package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 退票费用计算 Request VO")
@Data
public class RefundFeeCalculateReqVO {

    @Schema(description = "航段ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "航段ID不能为空")
    private Long segmentId;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "退票类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票类型不能为空")
    private Integer refundType;

    @Schema(description = "航班号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "航班号不能为空")
    private String flightNo;

    @Schema(description = "舱位等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "舱位等级不能为空")
    private String cabinClass;

    @Schema(description = "出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出发时间不能为空")
    private LocalDateTime departureTime;

}