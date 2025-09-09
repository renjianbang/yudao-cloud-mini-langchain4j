package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 退票申请审核 Request VO")
@Data
public class RefundApplicationApproveReqVO {

    @Schema(description = "退票申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票申请ID不能为空")
    private Long id;

    @Schema(description = "是否通过", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审核结果不能为空")
    private Boolean approved;

    @Schema(description = "审核备注")
    private String remarks;

}