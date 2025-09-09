package cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 航班航段分页 Request VO")
@Data
public class FlightSegmentsPageReqVO extends PageParam {

    @Schema(description = "关联的订单ID", example = "28186")
    private Long orderId;

    @Schema(description = "关联的乘客ID", example = "6371")
    private Long passengerId;

    @Schema(description = "航段类型 (1: 去程, 2: 返程)", example = "1")
    private Integer segmentType;

    @Schema(description = "航司二字码 (例如: MU, CA)")
    private String airlineCode;

    @Schema(description = "航班号 (例如: MU583)")
    private String flightNo;

    @Schema(description = "出发机场三字码 (例如: PVG)")
    private String departureAirportCode;

    @Schema(description = "到达机场三字码 (例如: LAX)")
    private String arrivalAirportCode;

    @Schema(description = "计划出发时间 (当地时间)")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] departureTime;

    @Schema(description = "计划到达时间 (当地时间)")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] arrivalTime;

    @Schema(description = "舱位等级 (例如: ECONOMY, BUSINESS)")
    private String cabinClass;

    @Schema(description = "电子票号 (出票后回填)")
    private String ticketNo;

    @Schema(description = "航段状态 (10: 正常, 20: 已改签, 30: 已退票)", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间")
    private LocalDateTime updatedAt;

}