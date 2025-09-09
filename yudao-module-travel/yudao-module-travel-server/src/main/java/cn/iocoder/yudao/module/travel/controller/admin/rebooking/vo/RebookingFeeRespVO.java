package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 改签费用响应 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebookingFeeRespVO {

    @Schema(description = "改签手续费", example = "200.00")
    private BigDecimal changeFee;

    @Schema(description = "票价差额", example = "500.00")
    private BigDecimal fareDifference;

    @Schema(description = "服务费", example = "50.00")
    private BigDecimal serviceFee;

    @Schema(description = "总费用", example = "750.00")
    private BigDecimal totalFee;

}