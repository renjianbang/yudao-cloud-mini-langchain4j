package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 机场信息 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportInfoVO {

    @Schema(description = "机场三字码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PVG")
    private String code;

    @Schema(description = "机场名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海浦东国际机场")
    private String name;

    @Schema(description = "机场英文名称", example = "Shanghai Pudong International Airport")
    private String englishName;

    @Schema(description = "城市代码", example = "SHA")
    private String cityCode;

    @Schema(description = "城市名称", example = "上海")
    private String cityName;

    @Schema(description = "国家代码", example = "CN")
    private String countryCode;

    @Schema(description = "国家名称", example = "中国")
    private String countryName;

}