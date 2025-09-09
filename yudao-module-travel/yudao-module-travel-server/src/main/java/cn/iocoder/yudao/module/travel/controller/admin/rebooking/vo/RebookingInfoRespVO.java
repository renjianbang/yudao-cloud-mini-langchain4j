package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 改签信息响应 VO")
@Data
public class RebookingInfoRespVO {

    @Schema(description = "订单信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private OrderInfoVO orderInfo;
    
    @Schema(description = "乘客列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PassengerVO> passengers;
    
    @Schema(description = "航段列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FlightSegmentVO> segments;

    @Schema(description = "订单信息")
    @Data
    public static class OrderInfoVO {
        @Schema(description = "订单ID", example = "1")
        private Long id;
        
        @Schema(description = "订单号", example = "ORD20241201001")
        private String orderNo;
        
        @Schema(description = "订单状态", example = "30")
        private Integer orderStatus;
        
        @Schema(description = "联系人姓名", example = "张三")
        private String contactName;
        
        @Schema(description = "联系人电话", example = "13812345678")
        private String contactPhone;
    }

    @Schema(description = "乘客信息")
    @Data
    public static class PassengerVO {
        @Schema(description = "乘客ID", example = "1")
        private Long id;
        
        @Schema(description = "中文姓名", example = "张三")
        private String chineseName;
        
        @Schema(description = "英文姓名", example = "ZHANG SAN")
        private String englishName;
        
        @Schema(description = "证件号码", example = "310101198503150123")
        private String idNumber;
    }

    @Schema(description = "航段信息")
    @Data
    public static class FlightSegmentVO {
        @Schema(description = "航段ID", example = "1")
        private Long id;
        
        @Schema(description = "乘客ID", example = "1")
        private Long passengerId;
        
        @Schema(description = "航班号", example = "MU583")
        private String flightNo;
        
        @Schema(description = "航司代码", example = "MU")
        private String airlineCode;
        
        @Schema(description = "出发机场", example = "PVG")
        private String departureAirportCode;
        
        @Schema(description = "到达机场", example = "LAX")
        private String arrivalAirportCode;
        
        @Schema(description = "出发时间", example = "2024-12-15 08:00:00")
        private String departureTime;
        
        @Schema(description = "到达时间", example = "2024-12-15 12:00:00")
        private String arrivalTime;
        
        @Schema(description = "舱位等级", example = "BUSINESS")
        private String cabinClass;
        
        @Schema(description = "航段状态", example = "10")
        private Integer status;
    }
}