package cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 航班航段新增/修改 Request VO")
@Data
public class FlightSegmentsSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15709")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28186")
    @NotNull(message = "关联的订单ID不能为空")
    private Long orderId;

    @Schema(description = "关联的乘客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6371")
    @NotNull(message = "关联的乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "航段类型 (1: 去程, 2: 返程)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "航段类型 (1: 去程, 2: 返程)不能为空")
    private Integer segmentType;

    @Schema(description = "航司二字码 (例如: MU, CA)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "航司二字码 (例如: MU, CA)不能为空")
    private String airlineCode;

    @Schema(description = "航班号 (例如: MU583)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "航班号 (例如: MU583)不能为空")
    private String flightNo;

    @Schema(description = "出发机场三字码 (例如: PVG)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "出发机场三字码 (例如: PVG)不能为空")
    private String departureAirportCode;

    @Schema(description = "到达机场三字码 (例如: LAX)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "到达机场三字码 (例如: LAX)不能为空")
    private String arrivalAirportCode;

    @Schema(description = "计划出发时间 (当地时间)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划出发时间 (当地时间)不能为空")
    private LocalDateTime departureTime;

    @Schema(description = "计划到达时间 (当地时间)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划到达时间 (当地时间)不能为空")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级 (例如: ECONOMY, BUSINESS)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "舱位等级 (例如: ECONOMY, BUSINESS)不能为空")
    private String cabinClass;

    @Schema(description = "电子票号 (出票后回填)")
    private String ticketNo;

    @Schema(description = "航段状态 (10: 正常, 20: 已改签, 30: 已退票)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "航段状态 (10: 正常, 20: 已改签, 30: 已退票)不能为空")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最后更新时间不能为空")
    private LocalDateTime updatedAt;

}