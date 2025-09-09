package cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 订单操作日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderOperationsLogRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16536")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "关联的订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24196")
    @ExcelProperty("关联的订单ID")
    private Long orderId;

    @Schema(description = "操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("操作类型 (例如: CREATE_ORDER, PAY_SUCCESS, TICKET_ISSUED, APPLY_CHANGE, CONFIRM_REFUND)")
    private String operationType;

    @Schema(description = "操作前的数据快照 (JSON格式)")
    @ExcelProperty("操作前的数据快照 (JSON格式)")
    private String contentBefore;

    @Schema(description = "操作后的数据快照 (JSON格式)")
    @ExcelProperty("操作后的数据快照 (JSON格式)")
    private String contentAfter;

    @Schema(description = "操作人ID", example = "7819")
    @ExcelProperty("操作人ID")
    private Long operatorId;

    @Schema(description = "操作人姓名", example = "张三")
    @ExcelProperty("操作人姓名")
    private String operatorName;

    @Schema(description = "操作备注", example = "随便")
    @ExcelProperty("操作备注")
    private String remark;

    @Schema(description = "操作时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("操作时间")
    private LocalDateTime createdAt;

}
