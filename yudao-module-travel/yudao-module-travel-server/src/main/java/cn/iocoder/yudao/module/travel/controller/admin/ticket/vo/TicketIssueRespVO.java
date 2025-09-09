package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 出票响应 VO")
@Data
public class TicketIssueRespVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    private Long orderId;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ORD20241201001")
    private String orderNo;

    @Schema(description = "电子票号列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> ticketNos;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currency;

    @Schema(description = "出票状态 (10:成功, 20:失败, 30:处理中)", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer status;

    @Schema(description = "结果消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "出票时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime issueTime;

}