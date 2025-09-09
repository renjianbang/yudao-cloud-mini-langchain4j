package cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 航班航段 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FlightSegmentsRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15709")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28186")
    @ExcelProperty("关联的订单ID")
    private Long orderId;

    @Schema(description = "关联的乘客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6371")
    @ExcelProperty("关联的乘客ID")
    private Long passengerId;

    @Schema(description = "航段类型 (1: 去程, 2: 返程)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("航段类型 (1: 去程, 2: 返程)")
    private Integer segmentType;

    @Schema(description = "航司二字码 (例如: MU, CA)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("航司二字码 (例如: MU, CA)")
    private String airlineCode;

    @Schema(description = "航班号 (例如: MU583)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("航班号 (例如: MU583)")
    private String flightNo;

    @Schema(description = "出发机场三字码 (例如: PVG)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出发机场三字码 (例如: PVG)")
    private String departureAirportCode;

    @Schema(description = "到达机场三字码 (例如: LAX)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("到达机场三字码 (例如: LAX)")
    private String arrivalAirportCode;

    @Schema(description = "计划出发时间 (当地时间)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划出发时间 (当地时间)")
    private LocalDateTime departureTime;

    @Schema(description = "计划到达时间 (当地时间)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划到达时间 (当地时间)")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级 (例如: ECONOMY, BUSINESS)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("舱位等级 (例如: ECONOMY, BUSINESS)")
    private String cabinClass;

    @Schema(description = "电子票号 (出票后回填)")
    @ExcelProperty("电子票号 (出票后回填)")
    private String ticketNo;

    @Schema(description = "航段状态 (10: 正常, 20: 已改签, 30: 已退票)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("航段状态 (10: 正常, 20: 已改签, 30: 已退票)")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("最后更新时间")
    private LocalDateTime updatedAt;

}
