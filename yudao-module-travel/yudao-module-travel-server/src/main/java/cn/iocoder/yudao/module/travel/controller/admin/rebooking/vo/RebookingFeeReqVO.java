package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 改签费用计算请求 VO")
@Data
public class RebookingFeeReqVO {

    @Schema(description = "原航段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "原航段ID不能为空")
    private Long originalSegmentId;

    @Schema(description = "新航班信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "新航班信息不能为空")
    private FlightInfoVO newFlightInfo;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "航班信息")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightInfoVO {
        @Schema(description = "航班号", example = "MU585")
        private String flightNo;

        @Schema(description = "航司代码", example = "MU")
        private String airlineCode;

        @Schema(description = "出发机场代码", example = "PVG")
        private String departureAirportCode;

        @Schema(description = "到达机场代码", example = "LAX")
        private String arrivalAirportCode;

        @Schema(description = "出发时间")
        private LocalDateTime departureTime;

        @Schema(description = "到达时间")
        private LocalDateTime arrivalTime;

        @Schema(description = "舱位等级", example = "BUSINESS")
        private String cabinClass;

        @Schema(description = "票价")
        private BigDecimal price;
    }

}