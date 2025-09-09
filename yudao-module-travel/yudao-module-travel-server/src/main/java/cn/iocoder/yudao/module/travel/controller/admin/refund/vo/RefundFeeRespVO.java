package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 退票费用计算 Response VO")
@Data
public class RefundFeeRespVO {

    @Schema(description = "原票价")
    private BigDecimal originalTicketPrice;

    @Schema(description = "退票费")
    private BigDecimal refundFee;

    @Schema(description = "退票金额")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额")
    private BigDecimal actualRefundAmount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "费用明细")
    private List<FeeDetail> feeDetails;

    @Data
    public static class FeeDetail {
        @Schema(description = "费用类型")
        private String type;

        @Schema(description = "金额")
        private BigDecimal amount;

        @Schema(description = "说明")
        private String description;
    }

}