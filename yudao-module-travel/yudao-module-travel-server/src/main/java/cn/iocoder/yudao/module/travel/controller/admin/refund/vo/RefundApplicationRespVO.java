package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 退票申请 Response VO")
@Data
public class RefundApplicationRespVO {

    @Schema(description = "退票申请ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "航段ID")
    private Long segmentId;

    @Schema(description = "乘客ID")
    private Long passengerId;

    @Schema(description = "乘客姓名")
    private String passengerName;

    @Schema(description = "航班号")
    private String flightNo;

    @Schema(description = "航司代码")
    private String airlineCode;

    @Schema(description = "航司名称")
    private String airlineName;

    @Schema(description = "出发机场代码")
    private String departureAirportCode;

    @Schema(description = "出发机场名称")
    private String departureAirportName;

    @Schema(description = "到达机场代码")
    private String arrivalAirportCode;

    @Schema(description = "到达机场名称")
    private String arrivalAirportName;

    @Schema(description = "出发时间")
    private LocalDateTime departureTime;

    @Schema(description = "到达时间")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级")
    private String cabinClass;

    @Schema(description = "原票价")
    private BigDecimal originalTicketPrice;

    @Schema(description = "退票费")
    private BigDecimal refundFee;

    @Schema(description = "退票金额")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额")
    private BigDecimal actualRefundAmount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "退票原因")
    private String reason;

    @Schema(description = "退票类型")
    private Integer refundType;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "审核人")
    private String approver;

    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    @Schema(description = "审核备注")
    private String approveRemarks;

    @Schema(description = "申请备注")
    private String remarks;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人")
    private String creator;

}