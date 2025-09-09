package cn.iocoder.yudao.module.travel.controller.admin.ticket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 机场信息 VO")
@Data
public class AirportInfoVO {

    @Schema(description = "机场三字码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PVG")
    private String code;

    @Schema(description = "机场名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海浦东国际机场")
    private String name;

    @Schema(description = "所属城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海")
    private String city;

    @Schema(description = "国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CN")
    private String country;

    @Schema(description = "时区", requiredMode = Schema.RequiredMode.REQUIRED, example = "Asia/Shanghai")
    private String timezone;

}