package cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单操作日志新增/修改 Request VO")
@Data
public class OrderOperationsLogSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16536")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24196")
    @NotNull(message = "关联的订单ID不能为空")
    private Long orderId;

    @Schema(description = "操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)不能为空")
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

    @Schema(description = "操作时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "操作时间不能为空")
    private LocalDateTime createdAt;

}