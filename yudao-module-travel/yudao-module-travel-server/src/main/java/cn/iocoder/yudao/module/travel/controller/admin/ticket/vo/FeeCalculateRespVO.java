package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 费用计算响应 VO")
@Data
public class FeeCalculateRespVO {

    @Schema(description = "费用明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FeeDetail> fees;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currency;

    @Schema(description = "费用分解", requiredMode = Schema.RequiredMode.REQUIRED)
    private FeeBreakdown breakdown;

    @Data
    public static class FeeDetail {
        @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED)
        private String feeType;

        @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal amount;

        @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
        private String currency;

        @Schema(description = "费用描述", requiredMode = Schema.RequiredMode.REQUIRED)
        private String description;
    }

    @Data
    public static class FeeBreakdown {
        @Schema(description = "基础票价", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal basePrice;

        @Schema(description = "税费合计", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal taxes;

        @Schema(description = "其他费用合计", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal fees;
    }
}