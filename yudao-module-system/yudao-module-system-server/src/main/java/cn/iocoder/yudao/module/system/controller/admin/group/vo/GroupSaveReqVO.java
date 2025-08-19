package cn.iocoder.yudao.module.system.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 用户组新增/修改 Request VO")
@Data
public class GroupSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9643")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "名字不能为空")
    private String name;

    @Schema(description = "描述", example = "你说的对")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}