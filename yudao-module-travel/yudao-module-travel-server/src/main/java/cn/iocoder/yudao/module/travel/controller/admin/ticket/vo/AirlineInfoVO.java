package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 航司信息 VO")
@Data
public class AirlineInfoVO {

    @Schema(description = "航司二字码", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU")
    private String code;

    @Schema(description = "航司名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国东方航空")
    private String name;

    @Schema(description = "航司全称", requiredMode = Schema.RequiredMode.REQUIRED, example = "China Eastern Airlines")
    private String fullName;

    @Schema(description = "国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CN")
    private String country;

}