package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 费用计算请求 VO")
@Data
public class FeeCalculateReqVO {

    @Schema(description = "乘客信息（简化版）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "乘客信息不能为空")
    private List<PassengerSimpleInfo> passengers;

    @Schema(description = "航段信息（简化版）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "航段信息不能为空")
    private List<FlightSegmentSimpleInfo> flightSegments;

    @Schema(description = "目标币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "币种不能为空")
    private String currency;

    @Data
    public static class PassengerSimpleInfo {
        @Schema(description = "乘客类型 (1:成人, 2:儿童, 3:婴儿)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "乘客类型不能为空")
        private Integer passengerType;

        @Schema(description = "年龄", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "年龄不能为空")
        private Integer age;
    }

    @Data
    public static class FlightSegmentSimpleInfo {
        @Schema(description = "航司二字码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "航司二字码不能为空")
        private String airlineCode;

        @Schema(description = "航班号", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "航班号不能为空")
        private String flightNo;

        @Schema(description = "出发机场三字码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "出发机场三字码不能为空")
        private String departureAirportCode;

        @Schema(description = "到达机场三字码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "到达机场三字码不能为空")
        private String arrivalAirportCode;

        @Schema(description = "出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "出发时间不能为空")
        private LocalDateTime departureTime;

        @Schema(description = "舱位等级", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "舱位等级不能为空")
        private String cabinClass;
    }
}