package cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单费用明细分页 Request VO")
@Data
public class OrderFeesPageReqVO extends PageParam {

    @Schema(description = "关联的订单ID", example = "22452")
    private Long orderId;

    @Schema(description = "关联的乘客ID (某些费用可能与特定乘客关联)", example = "24834")
    private Long passengerId;

    @Schema(description = "费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)", example = "1")
    private String feeType;

    @Schema(description = "费用金额")
    private BigDecimal amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "费用描述", example = "你猜")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间")
    private LocalDateTime updatedAt;

}