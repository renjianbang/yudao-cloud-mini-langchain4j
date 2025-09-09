package cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单操作日志分页 Request VO")
@Data
public class OrderOperationsLogPageReqVO extends PageParam {

    @Schema(description = "关联的订单ID", example = "24196")
    private Long orderId;

    @Schema(description = "操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)", example = "2")
    private String operationType;

    @Schema(description = "操作前的数据快照 (JSON格式)")
    private String contentBefore;

    @Schema(description = "操作后的数据快照 (JSON格式)")
    private String contentAfter;

    @Schema(description = "操作人ID", example = "7819")
    private Long operatorId;

    @Schema(description = "操作人姓名", example = "张三")
    private String operatorName;

    @Schema(description = "操作备注", example = "随便")
    private String remark;

    @Schema(description = "操作时间")
    private LocalDateTime createdAt;

}