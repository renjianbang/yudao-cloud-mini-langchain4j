package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 改签申请响应 VO")
@Data
public class RebookingApplicationRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long orderId;

    @Schema(description = "原航段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long originalSegmentId;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long passengerId;

    @Schema(description = "乘客姓名", example = "张三")
    private String passengerName;

    @Schema(description = "新航班号", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU585")
    private String newFlightNo;

    @Schema(description = "新航司代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU")
    private String newAirlineCode;

    @Schema(description = "新出发机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PVG")
    private String newDepartureAirportCode;

    @Schema(description = "新到达机场代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "LAX")
    private String newArrivalAirportCode;

    @Schema(description = "新出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime newDepartureTime;

    @Schema(description = "新到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime newArrivalTime;

    @Schema(description = "新舱位等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "BUSINESS")
    private String newCabinClass;

    @Schema(description = "改签费用", example = "200.00")
    private BigDecimal changeFee;

    @Schema(description = "票价差额", example = "500.00")
    private BigDecimal fareDifference;

    @Schema(description = "总费用", example = "750.00")
    private BigDecimal totalFee;

    @Schema(description = "改签原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间变更")
    private String reason;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer status;

    @Schema(description = "状态名称", example = "待审核")
    private String statusName;

    @Schema(description = "审核人", example = "admin")
    private String approver;

    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    @Schema(description = "备注", example = "紧急情况需要改签")
    private String remarks;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}