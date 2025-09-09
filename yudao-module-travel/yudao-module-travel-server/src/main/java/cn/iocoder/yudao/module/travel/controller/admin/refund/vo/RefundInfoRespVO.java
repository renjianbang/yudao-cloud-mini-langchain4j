package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 可退票信息 Response VO")
@Data
public class RefundInfoRespVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "可退票航段")
    private List<RefundableSegment> segments;

    @Schema(description = "乘客信息")
    private List<PassengerInfo> passengers;

    @Data
    public static class RefundableSegment {
        @Schema(description = "航段ID")
        private Long id;

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
        private String departureTime;

        @Schema(description = "到达时间")
        private String arrivalTime;

        @Schema(description = "舱位等级")
        private String cabinClass;

        @Schema(description = "票价")
        private java.math.BigDecimal ticketPrice;

        @Schema(description = "是否可退票")
        private Boolean canRefund;
    }

    @Data
    public static class PassengerInfo {
        @Schema(description = "乘客ID")
        private Long id;

        @Schema(description = "乘客姓名")
        private String name;

        @Schema(description = "证件类型")
        private Integer documentType;

        @Schema(description = "证件号码")
        private String documentNo;

        @Schema(description = "联系电话")
        private String phoneNumber;
    }

}