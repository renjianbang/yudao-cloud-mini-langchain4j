package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - 出票请求 VO")
@Data
public class TicketIssueReqVO {

    @Schema(description = "订单ID", example = "123")
    private Long orderId;

    @Schema(description = "订单号", example = "ORD20241201001")
    private String orderNo;

    @Schema(description = "乘客信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "乘客信息不能为空")
    private List<PassengerInfo> passengers;

    @Schema(description = "航段信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "航段信息不能为空")
    private List<FlightSegmentInfo> flightSegments;

    @Schema(description = "费用信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "费用信息不能为空")
    private List<FeeInfo> fees;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "币种不能为空")
    private String currency;

    @Schema(description = "备注")
    private String remark;

    @Data
    public static class PassengerInfo {
        @Schema(description = "乘客ID")
        private Long id;

        @Schema(description = "乘客类型 (1:成人, 2:儿童, 3:婴儿)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "乘客类型不能为空")
        private Integer passengerType;

        @Schema(description = "姓（拼音）", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "姓（拼音）不能为空")
        private String lastName;

        @Schema(description = "名（拼音）", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "名（拼音）不能为空")
        private String firstName;

        @Schema(description = "姓（中文）")
        private String lastNameCn;

        @Schema(description = "名（中文）")
        private String firstNameCn;

        @Schema(description = "性别 (1:男, 2:女)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "性别不能为空")
        private Integer gender;

        @Schema(description = "出生日期", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "出生日期不能为空")
        private LocalDate birthday;

        @Schema(description = "国籍代码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "国籍代码不能为空")
        private String nationality;

        @Schema(description = "证件类型 (1:身份证, 2:护照, 3:港澳通行证, 4:台胞证)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "证件类型不能为空")
        private Integer documentType;

        @Schema(description = "证件号码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "证件号码不能为空")
        private String documentNo;

        @Schema(description = "证件有效期")
        private LocalDate documentExpiry;

        @Schema(description = "手机号")
        private String mobile;

        @Schema(description = "邮箱")
        private String email;
    }

    @Data
    public static class FlightSegmentInfo {
        @Schema(description = "航段ID")
        private Long id;

        @Schema(description = "航段类型 (1:去程, 2:返程)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "航段类型不能为空")
        private Integer segmentType;

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

        @Schema(description = "到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "到达时间不能为空")
        private LocalDateTime arrivalTime;

        @Schema(description = "舱位等级", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "舱位等级不能为空")
        private String cabinClass;
    }

    @Data
    public static class FeeInfo {
        @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "费用类型不能为空")
        private String feeType;

        @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "费用金额不能为空")
        private BigDecimal amount;

        @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "币种不能为空")
        private String currency;

        @Schema(description = "费用描述")
        private String description;
    }
}