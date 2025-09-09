package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 航班验证响应 VO")
@Data
public class FlightValidationRespVO {

    @Schema(description = "是否有效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean valid;

    @Schema(description = "航班信息")
    private FlightInfo flightInfo;

    @Schema(description = "验证失败原因")
    private String reason;

    @Data
    public static class FlightInfo {
        @Schema(description = "航司代码", requiredMode = Schema.RequiredMode.REQUIRED)
        private String airlineCode;

        @Schema(description = "航班号", requiredMode = Schema.RequiredMode.REQUIRED)
        private String flightNo;

        @Schema(description = "出发机场", requiredMode = Schema.RequiredMode.REQUIRED)
        private String departureAirport;

        @Schema(description = "到达机场", requiredMode = Schema.RequiredMode.REQUIRED)
        private String arrivalAirport;

        @Schema(description = "出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
        private LocalDateTime departureTime;

        @Schema(description = "到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
        private LocalDateTime arrivalTime;

        @Schema(description = "机型", requiredMode = Schema.RequiredMode.REQUIRED)
        private String aircraft;

        @Schema(description = "可用座位数", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer availableSeats;

        @Schema(description = "航班状态", requiredMode = Schema.RequiredMode.REQUIRED)
        private String status;
    }
}