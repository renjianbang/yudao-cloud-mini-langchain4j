package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 退票申请取消 Request VO")
@Data
public class RefundApplicationCancelReqVO {

    @Schema(description = "退票申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票申请ID不能为空")
    private Long id;

    @Schema(description = "取消原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "取消原因不能为空")
    private String reason;

}