package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Schema(description = "管理后台 - 航班搜索请求 VO")
@Data
public class FlightSearchReqVO {

    @Schema(description = "出发机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PVG")
    @NotBlank(message = "出发机场不能为空")
    private String departureAirportCode;

    @Schema(description = "到达机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "LAX")
    @NotBlank(message = "到达机场不能为空")
    private String arrivalAirportCode;

    @Schema(description = "出发日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-12-15")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @Schema(description = "舱位等级", example = "ECONOMY")
    private String cabinClass;

}