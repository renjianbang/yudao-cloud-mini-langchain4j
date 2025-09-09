package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 航班搜索响应 VO")
@Data
public class FlightSearchRespVO {

    @Schema(description = "航班号", example = "MU583")
    private String flightNo;

    @Schema(description = "航司代码", example = "MU")
    private String airlineCode;

    @Schema(description = "航司名称", example = "中国东方航空")
    private String airlineName;

    @Schema(description = "出发机场代码", example = "PVG")
    private String departureAirportCode;

    @Schema(description = "出发机场名称", example = "上海浦东国际机场")
    private String departureAirportName;

    @Schema(description = "到达机场代码", example = "LAX")
    private String arrivalAirportCode;

    @Schema(description = "到达机场名称", example = "洛杉矶国际机场")
    private String arrivalAirportName;

    @Schema(description = "出发时间")
    private LocalDateTime departureTime;

    @Schema(description = "到达时间")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级", example = "ECONOMY")
    private String cabinClass;

    @Schema(description = "票价")
    private BigDecimal price;

    @Schema(description = "剩余座位数", example = "5")
    private Integer availableSeats;

}