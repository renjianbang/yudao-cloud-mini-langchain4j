package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 航空公司信息 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineInfoVO {

    @Schema(description = "航司二字码", requiredMode = Schema.RequiredMode.REQUIRED, example = "MU")
    private String code;

    @Schema(description = "航司名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国东方航空")
    private String name;

    @Schema(description = "航司英文名称", example = "China Eastern Airlines")
    private String englishName;

    @Schema(description = "国家代码", example = "CN")
    private String countryCode;

    @Schema(description = "国家名称", example = "中国")
    private String countryName;

}