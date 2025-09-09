package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 改签申请审核请求 VO")
@Data
public class RebookingApproveReqVO {

    @Schema(description = "改签申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "改签申请ID不能为空")
    private Long id;

    @Schema(description = "审核结果 (20: 已通过, 30: 已拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "审核结果不能为空")
    private Integer status;

    @Schema(description = "审核意见", example = "同意改签申请")
    private String remarks;

}